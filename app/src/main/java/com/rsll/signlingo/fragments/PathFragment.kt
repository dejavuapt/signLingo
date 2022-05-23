package com.rsll.signlingo.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import com.rsll.signlingo.R
import com.rsll.signlingo.LevelFstTrainingActivity
import com.rsll.signlingo.LevelThrTrainingActivity

class PathFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val v = inflater.inflate(R.layout.fragment_path, container, false)

        val btnLevel = v.findViewById<Button>(R.id.levelone_button);
        btnLevel.setOnClickListener{
            val intent = Intent(this@PathFragment.requireContext(), LevelFstTrainingActivity::class.java)
            startActivity(intent)
        }

        val btnThrLevel = v.findViewById<Button>(R.id.levelthree_button)
        btnThrLevel.setOnClickListener{
            val intent = Intent(this@PathFragment.requireContext(), LevelThrTrainingActivity::class.java)
            startActivity(intent)
        }

        return v
    }
}