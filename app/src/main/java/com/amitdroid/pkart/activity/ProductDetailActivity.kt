package com.amitdroid.pkart.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.edit
import androidx.lifecycle.lifecycleScope
import com.amitdroid.pkart.MainActivity
import com.amitdroid.pkart.databinding.ActivityProductDetailBinding
import com.amitdroid.pkart.roomdb.AppDatabase
import com.amitdroid.pkart.roomdb.ProductDao
import com.amitdroid.pkart.roomdb.ProductModel
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.models.SlideModel
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ProductDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProductDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProductDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val productId = intent.getStringExtra("id")
        if (productId != null) {
            getProductDetail(productId)
        } else {
            Toast.makeText(this, "Product ID not provided", Toast.LENGTH_SHORT).show()
            finish()
        }
    }

    private fun getProductDetail(proId: String) {
        Firebase.firestore.collection("products")
            .document(proId).get().addOnSuccessListener { documentSnapshot ->
                val list = documentSnapshot.get("productImages") as? ArrayList<String>
                val name = documentSnapshot.getString("productName")
                val productSp = documentSnapshot.getString("productSp")
                val productDesc = documentSnapshot.getString("productDescription")
                binding.textView10.text = name
                binding.textView11.text = productSp
                binding.textView12.text = productDesc

                val slideList = list?.map { SlideModel(it, ScaleTypes.CENTER_CROP) }

                cartAction(proId, name, productSp, documentSnapshot.getString("productCoverImg"))

                if (slideList != null) {
                    binding.imageSlider.setImageList(slideList)
                }
            }.addOnFailureListener {
                Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show()
            }
    }

    private fun cartAction(proId: String, name: String?, productSp: String?, productCoverImg: String?) {
        val productDao = AppDatabase.getInstance(this).productDao()

        if(productDao.isExit(proId) != null){
            binding.textView13.text="Go To Cart"
        }else{
            binding.textView13.text="Add to Cart"
        }

        binding.textView13.setOnClickListener {
            if(productDao.isExit(proId) != null){
                openCart()
            }
            else{

                addToCart(proId,name,productSp, productDao, productCoverImg)
            }

            }
    }

    private fun addToCart(
        proId: String,
        name: String?,
        productSp: String?,
        productDao: ProductDao,
        productCoverImg: String?
    ) {
        val data = ProductModel(proId, name, productSp, productCoverImg)
        lifecycleScope.launch(Dispatchers.IO) {
            productDao.insertProduct(data)
            binding.textView13.text = "Go to Cart"
        }
    }

    private fun openCart() {
        val preferences = this.getSharedPreferences("info", MODE_PRIVATE)
        val editor =preferences.edit()
        editor.putBoolean("isCart",true)
        editor.apply()
        startActivity(Intent(this,MainActivity::class.java))
        finish()

    }
}
