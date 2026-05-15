package com.example.kutirakone
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import android.widget.ImageView
import android.widget.EditText
import com.google.firebase.firestore.FirebaseFirestore


class MainActivity : AppCompatActivity() {
    private val db = FirebaseFirestore.getInstance()
    private lateinit var btnSelectImage: Button
    private lateinit var imageView: ImageView
    private lateinit var btnUpload: Button
    private lateinit var etMaterial: EditText
    private lateinit var etSize: EditText
    private lateinit var btnViewRequests: Button
    private lateinit var recyclerView: RecyclerView
    private lateinit var fabricList: ArrayList<Fabric>
    private lateinit var fabricAdapter: FabricAdapter

    private var imageUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnSelectImage = findViewById(R.id.btnSelectImage)
        imageView = findViewById(R.id.imageView)
        btnUpload = findViewById(R.id.btnUpload)
        etMaterial = findViewById(R.id.etMaterial)
        etSize = findViewById(R.id.etSize)
        btnViewRequests = findViewById(R.id.btnViewRequests)
        btnViewRequests.setOnClickListener {

            val intent = Intent(
                this,
                RequestsActivity::class.java
            )

            startActivity(intent)
        }
        recyclerView = findViewById(R.id.recyclerView)

        fabricList = ArrayList()

        fabricAdapter = FabricAdapter(fabricList) { position ->

            val fabric = fabricList[position]

            db.collection("fabrics")
                .document(fabric.id)
                .delete()
                .addOnSuccessListener {

                    fabricList.removeAt(position)

                    fabricAdapter.notifyItemRemoved(position)

                    Toast.makeText(
                        this,
                        "Deleted Successfully",
                        Toast.LENGTH_SHORT
                    ).show()
                }
        }

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = fabricAdapter
        db.collection("fabrics")
            .get()
            .addOnSuccessListener { result ->

                fabricList.clear()

                for (document in result) {

                    val fabric = Fabric(
                        document.id,
                        document.getString("material") ?: "",
                        document.getString("size") ?: ""
                    )

                    fabricList.add(fabric)
                }

                fabricAdapter.notifyDataSetChanged()
            }


        btnSelectImage.setOnClickListener {
            openGallery()
        }
        btnUpload.setOnClickListener {

            val material = etMaterial.text.toString()
            val size = etSize.text.toString()

            if (imageUri != null && material.isNotEmpty() && size.isNotEmpty()) {

                val fabric = hashMapOf(
                    "material" to material,
                    "size" to size
                )

                db.collection("fabrics")
                    .add(fabric)
                    .addOnSuccessListener {

                        fabricList.add(
                            Fabric(
                                "",
                                material,
                                size
                            )
                        )

                        fabricAdapter.notifyDataSetChanged()
                        Toast.makeText(
                            this,
                            "Uploaded to Firebase",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    .addOnFailureListener {
                        Toast.makeText(this,
                            "Upload Failed",
                            Toast.LENGTH_SHORT).show()
                    }
            } else {

                Toast.makeText(this,
                    "Please fill all fields",
                    Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, 100)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 100 && resultCode == Activity.RESULT_OK) {
            imageUri = data?.data
            imageView.setImageURI(imageUri)
            Toast.makeText(this, "Image Selected", Toast.LENGTH_SHORT).show()
        }
    }
}