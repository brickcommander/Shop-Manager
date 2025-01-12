package com.brickcommander.shop.fragments.purchase

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.brickcommander.shop.MainActivity
import com.brickcommander.shop.R
import com.brickcommander.shop.adapter.purchase.ItemAdapterForPurchaseDetailFragment
import com.brickcommander.shop.databinding.FragmentDetailsPurchaseBinding
import com.brickcommander.shop.model.Purchase
import com.brickcommander.shop.model.helperModel.PurchaseLite
import com.brickcommander.shop.util.convertLongToFormattedDate
import com.brickcommander.shop.util.toast
import com.brickcommander.shop.viewModel.PurchaseViewModel

class DetailPurchaseFragment : Fragment(R.layout.fragment_details_purchase) {
    companion object {
        const val TAG = "DetailPurchaseFragment"
    }
    private var _binding: FragmentDetailsPurchaseBinding? = null
    private val binding get() = _binding!!

    private var currPurchase: Purchase? = null
    private lateinit var purchaseViewModel: PurchaseViewModel
    private lateinit var mView: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailsPurchaseBinding.inflate(
            inflater,
            container,
            false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            Log.d(TAG, "onBackPressedDispatcher: ")
            view.findNavController().popBackStack(R.id.homePurchaseFragment, false) // Pop up to homeFragment
        }

        // using purchase lite to get the actual Purchase object by purchaseLite.purchaseId
        val purchaseLite: PurchaseLite? = arguments?.getParcelable("purchaseLite")
        Log.d(TAG, "onViewCreated: $purchaseLite")
        mView = view

        if(purchaseLite == null) {
            activity?.toast("Error Occured!")
            view.findNavController().navigate(R.id.action_detailFragment_to_homeFragment)
        }
        purchaseViewModel = (activity as MainActivity).purchaseViewModel

        // getting Purchase from PurchaseDao()
        currPurchase = purchaseViewModel.findPurchaseByPurchaseId(purchaseLite!!.purchaseId)
        if(currPurchase == null) {
            activity?.toast("Error Occured!")
            view.findNavController().navigate(R.id.action_detailFragment_to_homeFragment)
            return
        }
        Log.d(TAG, "onViewCreated: $currPurchase")

        binding.customerDetailsLayout.customerNameId.text = currPurchase!!.customer!!.name
        binding.customerDetailsLayout.totalAmountId.text = currPurchase!!.totalAmount.toString()
        binding.customerDetailsLayout.purchaseDateId.text = convertLongToFormattedDate(currPurchase!!.purchaseDate)
        binding.customerDetailsLayout.totalItemsId.text = currPurchase!!.items.size.toString()

        binding.itemListView.adapter = ItemAdapterForPurchaseDetailFragment(requireContext(), currPurchase!!.items)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
