package us.ait.minesweeper.model

object MinesweeperModel {

    public val EMPTY: Short = 0
    public val FLAG: Short = -1
    public val BOMB: Short = -2

    //model state will either be empty, FLAG, or BOMB
    val model = arrayOf(
        shortArrayOf(EMPTY, EMPTY, EMPTY, EMPTY, EMPTY),
        shortArrayOf(EMPTY, EMPTY, EMPTY, EMPTY, EMPTY),
        shortArrayOf(EMPTY, EMPTY, EMPTY, EMPTY, EMPTY),
        shortArrayOf(EMPTY, EMPTY, EMPTY, EMPTY, EMPTY),
        shortArrayOf(EMPTY, EMPTY, EMPTY, EMPTY, EMPTY)
    )

    public val UNCOVERED: Short = -4
    public val COVERED: Short = -5

    public var move = UNCOVERED

    //to keep track of which tiles are covered or not: states are COVERED and UNCOVERED
    val covered = arrayOf(
        shortArrayOf(COVERED, COVERED, COVERED, COVERED, COVERED),
        shortArrayOf(COVERED, COVERED, COVERED, COVERED, COVERED),
        shortArrayOf(COVERED, COVERED, COVERED, COVERED, COVERED),
        shortArrayOf(COVERED, COVERED, COVERED, COVERED, COVERED),
        shortArrayOf(COVERED, COVERED, COVERED, COVERED, COVERED)
    )

    var mineCount = 3
    var flagCount = mineCount

    var gameOver = false
    var gameWon = false

    var numRows = 5
    var numColumns = 5

    //place all the mines on the board
    fun placeMines() {
        var minesPlaced = 0

        while (minesPlaced <= mineCount){
        val rnd1 = (0..4).random()
        val rnd2 = (0..4).random()

            if (model[rnd1 ][rnd2] != BOMB){
                model[rnd1 ][rnd2] = BOMB
                minesPlaced++
            }
         }
    }

    // counts the number of mines surrounding that individual square
    fun setMineCount() {
        for (i in 0..4) {
            for (j in 0..4) {
                if (model[i][j] !== BOMB) {
                    var mineCounter = 0
                    if (i > 0 && j > 0 && model[i - 1][j - 1] === BOMB) {
                        mineCounter++
                    }
                    if (i > 0 && model[i - 1][j] === BOMB) {
                        mineCounter++
                    }
                    if (i > 0 && j < 4 && model[i - 1][j + 1] === BOMB) {
                        mineCounter++
                    }
                    if (j > 0 && model[i][j - 1] === BOMB) {
                        mineCounter++
                    }
                    if (j < 4 && model[i][j + 1] === BOMB) {
                        mineCounter++
                    }
                    if (i < 4 && j > 0 && model[i + 1][j - 1] === BOMB) {
                        mineCounter++
                    }
                    if (i < 4 && model[i + 1][j] === BOMB) {
                        mineCounter++
                    }
                    if (i < 4 && j < 4 && model[i + 1][j + 1] === BOMB) {
                        mineCounter++
                    }
                    model[i][j] = mineCounter.toShort()
                }
            }
        }
    }


    //you automatically lose if you flag a field with no bomb or uncover a bomb
    fun checkForLosingMove(){
        for (i in 0..4) {
            for (j in 0..4) {
                //you lose if you've flagged a square without a bomb
                if (model[i][j] != BOMB && model[i][j] == FLAG) {
                    gameOver = true
                }
                //and if an uncovered square contains a bomb
                if (model[i][j] == BOMB && covered[i][j] == UNCOVERED) {
                    gameOver = true
                }

            }
        }
    }

    //change the cover state at the field (x, y)
    fun setCoverState(x: Int, y: Int, state: Short) {
        model[x][y] = state
flagCount = when {
            //when the action is flagging, decrease the number of available flags
            (state == FLAG) -> flagCount  - 1
            //when the action is un-flagging, increase the number of available flags
            (state == COVERED) -> flagCount  + 1
            else -> flagCount
}
        checkForLosingMove()
        checkWin()

    }

    fun changeSquareState(tX: Int, tY: Int) {
        when (move) {
            UNCOVERED -> {
                //if in TRY mode, uncover the square
                setCoverState(tX, tY, UNCOVERED)
                //if the square is empty (i.e. no mines nearby), uncover all squares around it
                //if ((model[tX][tY].x).toInt() == 0) uncoverAllSquaresAround(tX, tY)
            }
            FLAG -> {
                //when in FLAG mode, toggle between FLAGGED and COVERED depending on
                // the current cover state of the square
                when (getCoverState(tX, tY)) {
                    FLAG -> setCoverState(tX, tY, COVERED)
                    COVERED -> {
                        //if player is not already out of flags, then they can flag the square
                        if (flagCount != 0) setCoverState(tX, tY, FLAG)
                    }
                }
            }
        }
    }

    fun getCoverState(x: Int, y: Int): Short = model[x][y]

    //if you can successfully place all three flags then you've won the game!
    fun checkWin(){
        if (flagCount == 0){
            gameOver = true
            gameWon = true
        }
    }

    fun resetModel() {
        for (i in 0..4) {
            for (j in 0..4) {
                model[i][j] = EMPTY
                covered[i][j] = COVERED
            }
        }
        move = UNCOVERED
        gameOver = false
        gameWon = false
        flagCount = mineCount
    }

}