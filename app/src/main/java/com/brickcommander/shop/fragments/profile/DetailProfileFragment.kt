package com.brickcommander.shop.fragments.profile

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.brickcommander.shop.MainActivity
import com.brickcommander.shop.R
import com.brickcommander.shop.databinding.FragmentDetailsProfileBinding
import com.brickcommander.shop.model.Profile
import com.brickcommander.shop.util.toast
import getObjectFromPreferences

class DetailProfileFragment : Fragment(R.layout.fragment_details_profile) {
    companion object {
        const val TAG = "DetailProfileFragment"
    }

    private var _binding: FragmentDetailsProfileBinding? = null
    private val binding get() = _binding!!

    private var profile: Profile? = null
    private lateinit var mView: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailsProfileBinding.inflate(
            inflater,
            container,
            false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        profile = getObjectFromPreferences(requireContext())
        Log.d(TAG, "onViewCreated: $profile")
        mView = view

        if(profile == null) {
            activity?.toast("Please create your profile")
            view.findNavController().navigate(R.id.action_detailProfileFragment2_to_addEditProfileFragment2)
            return
        }

        (requireActivity() as MainActivity).supportActionBar?.title = "Profile"

        binding.profileDetailLayout.shopNameId.text = profile!!.shopName
        binding.profileDetailLayout.ownerNameId.text = profile!!.owner
        binding.profileDetailLayout.mobileId.text = profile!!.mobile
        binding.profileDetailLayout.emailId.text = profile!!.email
        binding.profileDetailLayout.addressId.text = profile!!.address
        binding.profileDetailLayout.pincodeId.text = profile!!.pincode
        binding.profileDetailLayout.upiId.text = profile!!.upi
        binding.profileDetailLayout.gstinId.text = profile!!.gstin
    }

    private fun editItem(view: View) {
        view.findNavController().navigate(R.id.action_detailProfileFragment2_to_addEditProfileFragment2)
    }

    override fun onOptionsItemSelected(menuItem: MenuItem): Boolean {
        when (menuItem.itemId) {
            R.id.update_menu -> {
                editItem(mView)
            }
        }
        return super.onOptionsItemSelected(menuItem)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.update_menu, menu)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
