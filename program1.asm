START 200
MOVER AREG,='5'
MOVEM AREG,A
LOOP : MOVER AREG,A
MOVER CREG,B
ADD CREG,='1'
BC CREG,NEXT
LTORG ='5'
='1'
NEXT : SUB AREG,='1'
BC AREG,BACK
LAST : STOP
ORIGIN LOOP+2
MULT CREG,B
ORIGIN LAST+1
A : DS 1
BACK : EQU LOOP
B : DS 1
END  
='1'
