package com.rsicarelli.zeroglu.data

import android.util.Log
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.rsicarelli.zeroglu.domain.Tag
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.onFailure
import kotlinx.coroutines.channels.trySendBlocking
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.launch
import org.koin.core.annotation.Factory

private const val TagsCollectionReference = "Tags"

interface TagRemoteDataSource {

    val tags: Flow<List<Tag>>
}

@Factory
internal class TagRemoteDataSourceImpl : TagRemoteDataSource {

    override val tags: Flow<List<Tag>>
        get() = tags()

    private fun tags(): Flow<List<Tag>> =
        callbackFlow {
            Firebase.firestore.collection(TagsCollectionReference).addSnapshotListener { value, error ->
                require(error == null) { error?.localizedMessage ?: "Error on firebase" }
                requireNotNull(value)

                launch(Dispatchers.IO) {
                    value.map { async { it.toObject<Tag>() } }
                        .awaitAll()
                        .let { tags ->
                            trySendBlocking(tags)
                                .onFailure {
                                    Log.e("TagRemoteDataSource", "Error sending tag back to subscriber", error)
                                }
                        }
                }
            }.let { awaitClose(it::remove) }
        }
}
