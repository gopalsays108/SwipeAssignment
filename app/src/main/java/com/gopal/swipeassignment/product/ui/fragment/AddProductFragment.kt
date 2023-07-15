package com.gopal.swipeassignment.product.ui.fragment

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.google.android.material.snackbar.Snackbar
import com.gopal.swipeassignment.R
import com.gopal.swipeassignment.databinding.FragmentAddProductBinding
import com.gopal.swipeassignment.product.viewmodel.ProductViewModel
import com.gopal.swipeassignment.utilities.Constant.PICK_PHOTO_REQUEST_CODE
import com.gopal.swipeassignment.utilities.Constant.READ_STORAGE_PERMISSION_CODE
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.File
import java.util.Locale

class AddProductFragment : Fragment() {

    private lateinit var binding: FragmentAddProductBinding
    private val TAG = AddProductFragment::class.java.name
    private val productViewModel by viewModel<ProductViewModel>()
    private var providedType = ""
    private lateinit var photoPickerLauncher: ActivityResultLauncher<String>
    private lateinit var pickSingleMediaLauncher: ActivityResultLauncher<Intent>
    private var imageFile: File? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddProductBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setOnClickListners()
        attachObserver()

        // Initialize the ActivityResultLauncher
        photoPickerLauncher =
            registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
                uri?.let { selectedUri ->
                    // Handle the selected photo URI here
                    // You can use the URI to access the selected photo
                }
            }

        pickSingleMediaLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                if (it.resultCode != Activity.RESULT_OK) {
                    Toast.makeText(requireContext(), "Failed picking media.", Toast.LENGTH_SHORT)
                        .show()
                } else {
                    val uri = it.data?.data
                    if (uri != null) {
                        binding.productImage.setImageURI(uri)
                        imageFile =
                            File(getRealPathFromUri(uri)) // Get the actual file from the Uri
                    } else {
                        Toast.makeText(requireContext(), "Something went wrong", Toast.LENGTH_LONG)
                            .show()
                    }

                }
            }
    }

    private fun getRealPathFromUri(uri: Uri): String {
        var realPath = ""
        val cursor: Cursor? = requireActivity().contentResolver.query(uri, null, null, null, null)
        cursor?.use {
            if (it.moveToFirst()) {
                val columnIndex: Int = it.getColumnIndex(MediaStore.Images.Media.DATA)
                realPath = it.getString(columnIndex)
            }
        }
        return realPath
    }

    private fun attachObserver() {
        productViewModel.postedSucces.observe(viewLifecycleOwner) {
            if (it) {
                Snackbar.make(view!!, "Product Added", Snackbar.LENGTH_LONG).show()
                productViewModel.postedSucces.postValue(false)
                clearInputs()
            }
        }
        productViewModel.error.observe(viewLifecycleOwner) {
            Snackbar.make(view!!, "Something went wrong: $$it", Snackbar.LENGTH_LONG).show()
        }
        productViewModel.loading.observe(viewLifecycleOwner) {
            handleProgressBar(it)
        }
    }

    private fun clearInputs() {
        binding.productName.text?.clear()
        binding.enterPrice.text?.clear()
        binding.enterTax.text?.clear()
        imageFile = null
        binding.productImage.setImageDrawable(
            ResourcesCompat.getDrawable(
                resources,
                R.drawable.product_placeholder,
                null
            )
        )
        setCategorySelection(
            binding.gameSelection.getText().toString()
                .lowercase(), false
        )
        setCategorySelection(
            binding.furnitureSelectionTxt.getText().toString()
                .lowercase(), false
        )
        setCategorySelection(
            binding.electronnicSelectionTxt.getText().toString()
                .lowercase(), false
        )
    }

    private fun handleProgressBar(loading: Boolean) {
        if (loading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }

    private fun setOnClickListners() {
        binding.gameSelection.setOnClickListener {
            if (binding.gameSelection.getCompoundDrawables()[2] != null) {
                setCategorySelection(
                    binding.gameSelection.getText().toString()
                        .lowercase(), false
                )
            } else {
                setCategorySelection(
                    binding.gameSelection.getText().toString()
                        .lowercase(), true
                )
                setCategorySelection(
                    binding.furnitureSelectionTxt.getText().toString()
                        .lowercase(), false
                )
                setCategorySelection(
                    binding.electronnicSelectionTxt.getText().toString()
                        .lowercase(), false
                )
            }
        }

        binding.furnitureSelectionTxt.setOnClickListener {
            if (binding.furnitureSelectionTxt.getCompoundDrawables()[2] != null) {
                setCategorySelection(
                    binding.furnitureSelectionTxt.getText().toString()
                        .lowercase(), false
                )
            } else {
                setCategorySelection(
                    binding.gameSelection.getText().toString()
                        .lowercase(), false
                )
                setCategorySelection(
                    binding.furnitureSelectionTxt.getText().toString()
                        .lowercase(), true
                )
                setCategorySelection(
                    binding.electronnicSelectionTxt.getText().toString()
                        .lowercase(), false
                )
            }
        }

        binding.electronnicSelectionTxt.setOnClickListener {
            if (binding.electronnicSelectionTxt.getCompoundDrawables()[2] != null) {
                setCategorySelection(
                    binding.electronnicSelectionTxt.getText().toString()
                        .lowercase(), false
                )
            } else {
                setCategorySelection(
                    binding.gameSelection.getText().toString()
                        .lowercase(), false
                )
                setCategorySelection(
                    binding.furnitureSelectionTxt.getText().toString()
                        .lowercase(), false
                )
                setCategorySelection(
                    binding.electronnicSelectionTxt.getText().toString()
                        .lowercase(), true
                )
            }
        }

        binding.saveProduct.setOnClickListener {
            if (checkAllDetail()) {
                callPostApi()
            }
        }

        binding.productImage.setOnClickListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                selectPhotoFromStorage()
            } else
                checkPermission()
        }
    }

    private fun checkPermission() {
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            selectPhotoFromStorage()
        } else {
            requestStoragePermission()
        }
    }

    private fun requestStoragePermission() {
        ActivityCompat.requestPermissions(
            requireActivity(),
            arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
            READ_STORAGE_PERMISSION_CODE
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == READ_STORAGE_PERMISSION_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                selectPhotoFromStorage()
            } else {
                // Handle the case when the storage permission is denied
            }
        }
    }

    private fun selectPhotoFromStorage() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            // Setup pick 1 image/video
            pickSingleMediaLauncher.launch(
                Intent(MediaStore.ACTION_PICK_IMAGES)
            )
            return
        }
        photoPickerLauncher.launch("image/*")
    }

    private fun callPostApi() {
        lifecycleScope.launch {
            productViewModel.postProduct(
                binding.productName.text.toString(),
                providedType,
                binding.enterPrice.text.toString(),
                binding.enterTax.text.toString(),
                imageFile
            )
        }
    }

    private fun checkAllDetail(): Boolean {

        if (providedType.isEmpty()) {
            binding.categoryErrorTxt.visibility = View.VISIBLE
            return false
        }

        if (binding.productName.text.toString().isEmpty()) {
            Toast.makeText(requireContext(), "Please enter Product Name", Toast.LENGTH_LONG).show()
            return false
        }

        if (binding.enterPrice.text.toString().isEmpty()) {
            Toast.makeText(requireContext(), "Please enter Product Price", Toast.LENGTH_LONG).show()
            return false
        }

        if (binding.enterTax.text.toString().isEmpty()) {
            Toast.makeText(requireContext(), "Please enter Product Tax", Toast.LENGTH_LONG).show()
            return false
        }
        return true
    }

    private fun setCategorySelection(category: String, toSelect: Boolean) {
        when (category) {
            "game" -> if (toSelect) setCategoryText(
                binding.gameSelection,
                R.drawable.game_bg_50dp
            ) else removeCategoryText(
                binding.gameSelection,
                R.drawable.game_50dp_unselected,
                ResourcesCompat.getColor(resources, R.color.gamecolor, null)
            )

            "furniture" -> if (toSelect) setCategoryText(
                binding.furnitureSelectionTxt,
                R.drawable.furniture_bg_50dp
            ) else removeCategoryText(
                binding.furnitureSelectionTxt,
                R.drawable.furniture_bg_50dp_unselected,
                ResourcesCompat.getColor(resources, R.color.furnitureColor, null)
            )

            "electronics" -> if (toSelect) setCategoryText(
                binding.electronnicSelectionTxt,
                R.drawable.elec_bg_50dp
            ) else removeCategoryText(
                binding.electronnicSelectionTxt,
                R.drawable.electronic_bg_50dp_unselected,
                ResourcesCompat.getColor(resources, R.color.electronicCOlor, null)
            )
        }
    }

    private fun setCategoryText(textView: AppCompatTextView, drawable: Int) {
        textView.setBackgroundResource(drawable)
        textView.setTextColor(
            ResourcesCompat.getColor(
                resources, if (textView.text.toString()
                        .equals("fuel", ignoreCase = true)
                ) R.color.colorBlack else R.color.colorWhite, null
            )

        )
        textView.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_close_4, 0)
        providedType = textView.text.toString().lowercase(Locale.getDefault())
        binding.categoryErrorTxt.setVisibility(View.GONE)
    }

    private fun removeCategoryText(
        textView: AppCompatTextView,
        backgroundDrawable: Int,
        textColor: Int
    ) {
        textView.setBackgroundResource(backgroundDrawable)
        textView.setTextColor(textColor)
        textView.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0)
        if (providedType == textView.text.toString().lowercase(Locale.getDefault()))
            providedType = ""
    }

}