package com.lynn.billsapp.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.anychart.BuildConfig
import com.lynn.billsapp.R
import com.lynn.billsapp.databinding.FragmentSettingsBinding
import com.lynn.billsapp.models.LoginResponse
import com.lynn.billsapp.utils.Constants
import com.lynn.billsapp.viewModels.BillsViewModel


class settingsFragment : Fragment() {
    val binding:FragmentSettingsBinding?=null
    val billsViewModel:BillsViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragmen
        return inflater.inflate(R.layout.fragment_settings, container, false)
        return binding?.root
    }

    override fun onResume() {
        super.onResume()
//        binding?.tvAppVersion?.text="APP VERSION"${BuildConfig.VERSION_NAME}
    }

    fun Logout(loginResponse: LoginResponse){
        val sharedPreferences=requireContext().getSharedPreferences(Constants.PREFS, Context.MODE_PRIVATE)
        val editor=sharedPreferences.edit()
        editor.putString(Constants.USER_ID,loginResponse.userId)
        editor.putString(Constants.ACCESS_TOKEN,loginResponse.accessToken)
        editor.apply()

        requireContext().startActivity(Intent(requireContext(),Login::class.java))
    }

//    fun syncData(){
//        binding?.pbSync?.visibility=View.VISIBLE
//        billsViewModel.fetchRemoteBills()
//        val timer=object :CountDownTimer(1000,1000)
//
//                override fun onTick(p0:Long){
//
//                }
//                override fun onFinish(){
//                    binding?.pbSync?.hide()
//                }
//        timer.start()
//    }
}