package com.rsll.signlingo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.rsll.signlingo.fragments.PathFragment
import com.rsll.signlingo.fragments.ProfileFragment
import com.rsll.signlingo.fragments.SettingsFragment

class MainActivity : AppCompatActivity() {

    private val pathFragment = PathFragment()
    private val profileFragment = ProfileFragment()
    private val settingsFragment = SettingsFragment()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        replaceFragment(pathFragment)

        findViewById<BottomNavigationView>(R.id.bottom_navigation).setOnItemSelectedListener {
            item ->
            when(item.itemId){
                R.id.ic_path -> replaceFragment(pathFragment)
                R.id.ic_profile -> replaceFragment(profileFragment)
                R.id.ic_settings -> replaceFragment(settingsFragment)

            }
            true
        }

    }

    private fun replaceFragment(fragment: Fragment) {
        if(fragment != null){
            val transaction = supportFragmentManager.beginTransaction()
            transaction.replace(R.id.fragment_container, fragment)
            transaction.commit()
        }
    }
}