package com.brickcommander.shop.fragments.profile

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.brickcommander.shop.MainActivity
import com.brickcommander.shop.R
import com.brickcommander.shop.databinding.FragmentAddEditProfileBinding
import com.brickcommander.shop.model.Profile
import com.brickcommander.shop.util.toast
import getObjectFromPreferences
import saveObjectToPreferences

class AddEditProfileFragment : Fragment(R.layout.fragment_add_edit_profile) {
    companion object {
        const val TAG = "AddEditProfileFragment"
    }

    private var _binding: FragmentAddEditProfileBinding? = null
    private val binding get() = _binding!!
    private lateinit var mView: View

    private lateinit var shopNameEditText: EditText
    private lateinit var ownerNameEditText: EditText
    private lateinit var mobileEditText: EditText
    private lateinit var emailEditText: EditText
    private lateinit var addressEditText: EditText
    private lateinit var pinEditText: EditText
    private lateinit var upiEditText: EditText
    private lateinit var gstEditText: EditText

    private var profile: Profile? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddEditProfileBinding.inflate(
            inflater,
            container,
            false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mView = view

        shopNameEditText = binding.editTextShopName
        ownerNameEditText = binding.editTextOwnerName
        mobileEditText = binding.editTextMobile
        emailEditText = binding.editTextEmail
        addressEditText = binding.editTextAddress
        pinEditText = binding.editTextPincode
        upiEditText = binding.editTextUPI
        gstEditText = binding.editTextGSTIN

        profile = getObjectFromPreferences(requireContext())

        if (profile == null) (requireActivity() as MainActivity).supportActionBar?.title = "Add Profile"
        else (requireActivity() as MainActivity).supportActionBar?.title = "Update Profile"

        profile?.let {
            shopNameEditText.setText(it.shopName)
            ownerNameEditText.setText(it.owner)
            mobileEditText.setText(it.mobile)
            emailEditText.setText(it.email)
            addressEditText.setText(it.address)
            pinEditText.setText(it.pincode)
            upiEditText.setText(it.upi)
            gstEditText.setText(it.gstin)
        }
    }

    private fun saveItem(view: View) {
        if (shopNameEditText.text.toString().isEmpty()) {
            activity?.toast("Please Enter SHOP Name")
            return
        }
        else if (mobileEditText.text.toString().length != 10) {
            activity?.toast("Please Enter Valid Mobile Number")
            return
        }
        else if (pinEditText.text.toString().length != 6) {
            activity?.toast("Please Enter Valid PIN Code")
            return
        }
        else if(gstEditText.text.toString().length != 15) {
            activity?.toast("Please Enter Valid GST Number")
            return
        }

        var creatingNewProfile = false
        if (profile == null) {
            creatingNewProfile = true
            profile = Profile()
        }

        profile!!.shopName = shopNameEditText.text.toString().trim()
        profile!!.owner = ownerNameEditText.text.toString().trim()
        profile!!.mobile = mobileEditText.text.toString().trim()
        profile!!.email = emailEditText.text.toString().trim()
        profile!!.address = addressEditText.text.toString().trim()
        profile!!.pincode = pinEditText.text.toString().trim()
        profile!!.upi = upiEditText.text.toString().trim()
        profile!!.gstin = gstEditText.text.toString().trim()

        saveObjectToPreferences(requireContext(), profile!!)
        activity?.toast("Profile Saved successfully")
        if (creatingNewProfile) view.findNavController().navigate(R.id.action_addEditProfileFragment2_to_homeFragment)
        else view.findNavController().navigate(R.id.action_addEditProfileFragment2_to_detailProfileFragment2)
    }

    override fun onOptionsItemSelected(menuItem: MenuItem): Boolean {
        when (menuItem.itemId) {
            R.id.save_menu -> {
                saveItem(mView)
            }
        }
        return super.onOptionsItemSelected(menuItem)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        Log.d(TAG, "onCreateOptionsMenu: ")
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.save_menu, menu)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
