package com.example.kutirakone

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore

class RequestAdapter(
    private val requestList: ArrayList<SwapRequest>
) : RecyclerView.Adapter<RequestAdapter.RequestViewHolder>() {

    private val db = FirebaseFirestore.getInstance()

    class RequestViewHolder(itemView: View)
        : RecyclerView.ViewHolder(itemView) {

        val tvMaterial: TextView =
            itemView.findViewById(R.id.tvRequestMaterial)

        val tvSize: TextView =
            itemView.findViewById(R.id.tvRequestSize)

        val tvStatus: TextView =
            itemView.findViewById(R.id.tvRequestStatus)

        val btnAccept: Button =
            itemView.findViewById(R.id.btnAccept)

        val btnReject: Button =
            itemView.findViewById(R.id.btnReject)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RequestViewHolder {

        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_request, parent, false)

        return RequestViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: RequestViewHolder,
        position: Int
    ) {

        val request = requestList[position]

        holder.tvMaterial.text = request.material
        holder.tvSize.text = request.size
        holder.tvStatus.text = request.status

        holder.btnAccept.setOnClickListener {

            db.collection("swap_requests")
                .document(request.id)
                .update("status", "accepted")

            holder.tvStatus.text = "accepted"

            Toast.makeText(
                holder.itemView.context,
                "Request Accepted",
                Toast.LENGTH_SHORT
            ).show()
        }

        holder.btnReject.setOnClickListener {

            db.collection("swap_requests")
                .document(request.id)
                .update("status", "rejected")

            holder.tvStatus.text = "rejected"

            Toast.makeText(
                holder.itemView.context,
                "Request Rejected",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    override fun getItemCount(): Int {
        return requestList.size
    }
}