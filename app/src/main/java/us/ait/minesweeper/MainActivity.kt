package us.ait.minesweeper

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import us.ait.minesweeper.model.MinesweeperModel
import us.ait.minesweeper.model.MinesweeperModel.FLAG
import us.ait.minesweeper.model.MinesweeperModel.UNCOVERED
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //initialize flagText view text
        showFlagNum(MinesweeperModel.flagCount)

        //WHEN YOU CLICK THE RESTART BUTTON
        btnRestart.setOnClickListener {
            mineView.resetGame()
            toggleFlag.isChecked = false
            showMessage("game restarted")
        }

        //TOGGLE FLAG RULES
        toggleFlag.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                MinesweeperModel.move = FLAG
            } else {
                MinesweeperModel.move = UNCOVERED
            }
        }
    }

    fun showFlagNum(numFlags: Int) {
        flagText.text = numFlags.toString()
    }

    fun showMessage(msg: String) {
        Snackbar.make(mineView, msg, Snackbar.LENGTH_LONG).show()
    }


}