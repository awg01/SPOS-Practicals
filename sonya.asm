        START   200
        MOVER   AREG,=  '5'
        MOVEM   AREG,   X
L       MOVER   BREG,=    '2'
        ORIGIN  L+3
        LTORG
NEXT        ADD AREG,=  '1'
        SUB BREG,=  '1'
        BC  L,  BACK
        LTORG
BACK        EQU L
        ORIGIN  NEXT+5
        MULT    CREG,   4
        STOP
X       DS  1
        END