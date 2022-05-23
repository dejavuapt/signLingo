package com.rsll.signlingo

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.floatingactionbutton.FloatingActionButton

class LevelFstTrainingActivity : AppCompatActivity() {


    private val quests = 10
    var currentAnswers = 0

    //lattes
    lateinit var dialogSuccess: BottomSheetDialog


    //dataset tasks
    private val photosArray = arrayOf(
        R.drawable.letter1,
        R.drawable.letter2,
        R.drawable.letter3,
        R.drawable.letter4,
        R.drawable.letter5,
        R.drawable.letter6,
        R.drawable.letter8,
        R.drawable.letter9,
        R.drawable.letter10,
        R.drawable.letter11,
        R.drawable.letter12,
        R.drawable.letter13,
        R.drawable.letter14,
        R.drawable.letter15,
        R.drawable.letter16,
        R.drawable.letter17,
        R.drawable.letter18,
        R.drawable.letter19,
        R.drawable.letter20,
        R.drawable.letter21,
        R.drawable.letter22,
        R.drawable.letter23,
        R.drawable.letter24,
        R.drawable.letter25,
        R.drawable.letter26,
        R.drawable.letter27,
        R.drawable.letter29,
        R.drawable.letter29,
        R.drawable.letter30,
        R.drawable.letter31,
        R.drawable.letter32,
        R.drawable.letter33,
    )
    private val lettersArray = ('а'..'я').toMutableList()

    private var notCompletedRandomNumArray = photosArray.indices.toMutableList()

    //for dialogs
    private val wordsNices = listOf("Nice!", "Good!", "Yes!", "Amazing!")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_training)

        var rndPhoto = getRandomNumber()

        val placeSignImage = findViewById<ImageView>(R.id.signPhotoShape)
        val checkAnswerButton = findViewById<Button>(R.id.checkAnswerButton)
        val skipQuestButton = findViewById<Button>(R.id.skipQuestionButton)
        val backActionButton = findViewById<FloatingActionButton>(R.id.backActionButton)

        val editableText = findViewById<EditText>(R.id.editTextUserLetter)
        val progressAnswers = findViewById<ProgressBar>(R.id.progressAnswers)
        findViewById<TextView>(R.id.textTask).text = "What sign is?"

        placeSignImage.setImageResource(photosArray[rndPhoto])


        lateinit var userAnswerLetter: String



        checkAnswerButton.setOnClickListener {
            userAnswerLetter = editableText.text.toString() // конструкцию иф елсе в другую
            if (userAnswerLetter == "") {
                // nothing do? i think
            } else if (userAnswerLetter.lowercase().toCharArray()[0] == lettersArray[rndPhoto].lowercaseChar()) {
                currentAnswers++

                //dialog suc
                showDialogSuccess()
                // --------
                dialogSuccess.findViewById<Button>(R.id.nextButton)?.setOnClickListener{
                    rndPhoto = getRandomNumber()
                    placeSignImage.setImageResource(photosArray[rndPhoto])
                    editableText.text = null
                    ((currentAnswers * 100) / quests).also { progressAnswers.progress = it }
                    dialogSuccess.dismiss()
                }
            } else {
                // If not right answer
            }

            if(progressAnswers.progress == (quests-1)*100/quests) finish()
        }

        skipQuestButton.setOnClickListener{
            rndPhoto = getRandomNumber()
            placeSignImage.setImageResource(photosArray[rndPhoto])
        }

        backActionButton.setOnClickListener{
            finish()
        }
    }

    private fun getRandomNumber(): Int {
        //using method исключения
        if(notCompletedRandomNumArray.isEmpty()) {
            notCompletedRandomNumArray = photosArray
            .indices
            .toMutableList() }
        val rndnumValue = notCompletedRandomNumArray[(notCompletedRandomNumArray.indices).random()]
        notCompletedRandomNumArray.remove(rndnumValue)
        return rndnumValue
    }

    private fun showDialogSuccess() {
        dialogSuccess = BottomSheetDialog(this)
        dialogSuccess.setContentView(R.layout.layout_dialog_success)
        dialogSuccess.findViewById<TextView>(R.id.textAboutSuccessOrNot)?.text = wordsNices.random()
        dialogSuccess.show()
    }

}