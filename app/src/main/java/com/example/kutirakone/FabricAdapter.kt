package com.example.kutirakone

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import android.widget.Toast
import com.google.firebase.firestore.FirebaseFirestore

class FabricAdapter(
    private val fabricList: ArrayList<Fabric>,
    private val onDeleteClick: (Int) -> Unit
) :
    RecyclerView.Adapter<FabricAdapter.FabricViewHolder>() {
    private val db = FirebaseFirestore.getInstance()

    class FabricViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val tvMaterial: TextView = itemView.findViewById(R.id.tvMaterial)
        val tvSize: TextView = itemView.findViewById(R.id.tvSize)
        val btnSwap: Button = itemView.findViewById(R.id.btnSwap)
        val btnDelete: Button =
            itemView.findViewById(R.id.btnDelete)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FabricViewHolder {

        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_fabric, parent, false)

        return FabricViewHolder(view)
    }

    override fun onBindViewHolder(holder: FabricViewHolder, position: Int) {

        val fabric = fabricList[position]

        holder.tvMaterial.text = fabric.material
        holder.tvSize.text = fabric.size
        holder.btnSwap.setOnClickListener {

            val swapRequest = hashMapOf(

                "material" to fabric.material,
                "size" to fabric.size,
                "status" to "pending"
            )

            db.collection("swap_requests")
                .add(swapRequest)
                .addOnSuccessListener {

                    Toast.makeText(
                        holder.itemView.context,
                        "Swap Request Sent",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                .addOnFailureListener {

                    Toast.makeText(
                        holder.itemView.context,
                        "Request Failed",
                        Toast.LENGTH_SHORT
                    ).show()
                }
        }
        holder.btnDelete.setOnClickListener {
            onDeleteClick(position)
        }

    }

    override fun getItemCount(): Int {
        return fabricList.size
    }
}