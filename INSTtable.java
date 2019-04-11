/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package a1;

//INSTable.java
import java.util.HashMap;

public class INSTtable {

	HashMap<String, Integer> IS,AD,DL,RG,CC;

        //key=instruction name, value=opcode
	public INSTtable()
	{
		IS = new HashMap<>();
		AD=new HashMap<>();
		DL=new HashMap<>();
		RG = new HashMap<>();
		CC = new HashMap<>();

                IS.put("STOP",0);
		IS.put("ADD",1);
		IS.put("SUB",2);
		IS.put("MULT",3);
		IS.put("MOVER",4);
		IS.put("MOVEM",5);
		IS.put("COMP",6);
		IS.put("BC",7);
		IS.put("DIV",8);
		IS.put("READ",9);
		IS.put("PRINT",10);
		
		AD.put("START",1);
		AD.put("END",2);
		AD.put("ORIGIN",3);
		AD.put("EQU",4);
		AD.put("LTORG",5);

                DL.put("DC", 01);
		DL.put("DS", 02);

                RG.put("AREG",1);
		RG.put("BREG",2);
		RG.put("CREG",3);	
                
		CC.put("EQ",1);
		CC.put("LT",2);
		CC.put("GT",3);
		CC.put("LE",4);
		CC.put("GE",5);
		CC.put("NE",6);
		CC.put("ANY",7);		
	}

        //instruction belongs to which class return name of that class
	public String getType(String s)
	{
		s=s.toUpperCase();

                if(IS.containsKey(s))
			return "IS";
		else if(AD.containsKey(s))
			return "AD";
		else if(DL.containsKey(s))
			return "DL";		
		else if(RG.containsKey(s))
			return "RG";
		else if(CC.containsKey(s))
			return "CC";

		return "";
		
	}
	
        //return value of opcode of that instruction  
	public int getCode(String s)
	{
		s = s.toUpperCase();

                if(IS.containsKey(s))
			return IS.get(s);		
		else if(AD.containsKey(s))
			return AD.get(s);
		else if(DL.containsKey(s))
			return DL.get(s);
		else if(RG.containsKey(s))
			return RG.get(s);		
		else if(CC.containsKey(s))
			return CC.get(s);

		return -1;
	}
	
}