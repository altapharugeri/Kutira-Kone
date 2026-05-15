package com.example.kutirakone

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore

class RequestsActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var requestList: ArrayList<SwapRequest>
    private lateinit var requestAdapter: RequestAdapter

    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_requests)

        recyclerView = findViewById(R.id.requestRecyclerView)

        requestList = ArrayList()

        requestAdapter = RequestAdapter(requestList)

        recyclerView.layoutManager =
            LinearLayoutManager(this)

        recyclerView.adapter = requestAdapter

        db.collection("swap_requests")
            .get()
            .addOnSuccessListener { result ->

                requestList.clear()

                for (document in result) {

                    val request = SwapRequest(
                        document.id,
                        document.getString("material") ?: "",
                        document.getString("size") ?: "",
                        document.getString("status") ?: ""
                    )

                    requestList.add(request)
                }

                requestAdapter.notifyDataSetChanged()
            }
    }
}