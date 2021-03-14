package us.ait.minesweeper.ui


import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import us.ait.minesweeper.MainActivity
import us.ait.minesweeper.model.MinesweeperModel

class MinesweeperView (context: Context?, attrs: AttributeSet?) : View(context, attrs){
    private var paintLine: Paint
    private var paintText: Paint
    private val paintObjects = Paint()

    private val paintUnrevealedBoard = Paint()
    private val paintRevealedBoard = Paint()

    init{

        //painting the selected and unselected board squares
        paintRevealedBoard.color = Color.parseColor("#344648")
        paintUnrevealedBoard.color = Color.parseColor("#7D8E95")

        //painting the objects (bomb, flag, etc.) the peach color
        paintObjects.color = Color.parseColor("#FFBB98")
        paintObjects.textSize = 60f

        //painting the lines for the board
        paintLine = Paint()
        paintLine.color = Color.WHITE
        paintLine.strokeWidth = 5f
        paintLine.style = Paint.Style.STROKE

        //all the white text
        paintText = Paint()
        paintText.color = Color.WHITE
        paintText.textSize = 60f
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)

        paintText.textSize = height.toFloat() / 5f
        paintObjects.textSize = height.toFloat() / 5f

    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        canvas?.drawRect(0f, 0f, width.toFloat(), height.toFloat(), paintRevealedBoard)

        drawGameArea(canvas)
        drawObjects(canvas)
    }

    fun drawGameArea(canvas: Canvas) {
        // border
        canvas.drawRect(0f, 0f, width.toFloat(), height.toFloat(), paintLine)

        // four horizontal lines
        canvas.drawLine(
            0f, (height / 5).toFloat(), width.toFloat(), (height / 5).toFloat(),
            paintLine)
        canvas.drawLine(
            0f, (2 * height / 5).toFloat(), width.toFloat(),
            (2 * height / 5).toFloat(), paintLine
        )
        canvas.drawLine(
            0f, (3 * height / 5).toFloat(), width.toFloat(),
            (3 * height / 5).toFloat(), paintLine
        )
        canvas.drawLine(
            0f, (4 * height / 5).toFloat(), width.toFloat(),
            (4 * height / 5).toFloat(), paintLine
        )


        // four vertical lines
        canvas.drawLine(
            (width / 5).toFloat(), 0f, (width / 5).toFloat(), height.toFloat(),
            paintLine
        )
        canvas.drawLine(
            (2 * width / 5).toFloat(), 0f, (2 * width / 5).toFloat(), height.toFloat(),
            paintLine
        )
        canvas.drawLine(
            (3 * width / 5).toFloat(), 0f, (3 * width / 5).toFloat(), height.toFloat(),
            paintLine
        )
        canvas.drawLine(
            (4 * width / 5).toFloat(), 0f, (4 * width / 5).toFloat(), height.toFloat(),
            paintLine
        )

        }

    //draw all of the numbers and objects in the squares
    private fun drawObjects(canvas: Canvas) {
        for (i in 0 until 4) {
            for (j in 0 until 4) {
                drawObject(i, j, canvas)
            }
        }

    }

    //function that draws the revealed square color, bomb, #bombs nearby, or flag
    private fun drawObject(i: Int, j: Int, canvas: Canvas) {
        //get dimensions of each tile
        val width = width.toFloat() / 5f
        val height = height.toFloat() / 5f

        //if FLAGGED, draw little flag symbol
        if (MinesweeperModel.model[i][j] == MinesweeperModel.FLAG) {
            //add flag symbol
            canvas.drawText(">", i * width + (.15 * width).toFloat(), (j + 1) * height - (.15 * height).toFloat(), paintObjects)
        }

        //if the spot is not covered
        if (MinesweeperModel.covered[i][j] == MinesweeperModel.UNCOVERED) {
            //gray out the background
            canvas.drawRect(i * width, j * height, (i + 1) * width, (j + 1) * height, paintUnrevealedBoard)

    //add bomb if needed
            if (MinesweeperModel.model[i][j] == MinesweeperModel.BOMB) {
                //add bomb symbol
                canvas.drawText("o", i * width + (.15 * width).toFloat(), (j + 1) * height - (.15 * height).toFloat(), paintObjects)
            } else {
                canvas.drawText(MinesweeperModel.model[i][j].toString(), i * width + (.15 * width).toFloat(), (j + 1) * height - (.15 * height).toFloat(), paintObjects)
            }

        }
    }


    override fun onTouchEvent(event: MotionEvent?): Boolean {
        val tX = event!!.x.toInt() / (width / MinesweeperModel.numColumns)
        val tY = event!!.y.toInt() / (height / MinesweeperModel.numRows)


        //if the game not over then change the state of (tX, tY)
        if (!MinesweeperModel.gameOver && !MinesweeperModel.gameWon && (tX < MinesweeperModel.numColumns) && (tY < MinesweeperModel.numRows)) {

            if (MinesweeperModel.move == MinesweeperModel.FLAG && MinesweeperModel.getCoverState(tX, tY) == MinesweeperModel.COVERED && MinesweeperModel.flagCount == 0) (context as MainActivity).showMessage("no flags")
            MinesweeperModel.changeSquareState(tX, tY)
            (context as MainActivity).showFlagNum(MinesweeperModel.flagCount)
        }

        if (MinesweeperModel.gameOver) (context as MainActivity).showMessage("game over")
        if (MinesweeperModel.gameWon) (context as MainActivity).showMessage("you've won!")

        invalidate()

        return super.onTouchEvent(event)
    }

    public fun resetGame(){
        MinesweeperModel.resetModel()
        invalidate()
    }


}