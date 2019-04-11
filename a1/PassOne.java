/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package a1;	


//PassOne.java
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;

public class PassOne{
	int lc=0, lc1=0;
	int libtab_ptr=0,pooltab_ptr=0;
	int symIndex=-1,litIndex=-1;
	LinkedHashMap<String, TableRow> SYMTAB;
	ArrayList<TableRow> LITTAB;
	ArrayList<Integer> POOLTAB;
	private BufferedReader br;

	public PassOne()
	{
		SYMTAB =new LinkedHashMap<>();
		LITTAB=new ArrayList<>();
		POOLTAB=new ArrayList<>();
		lc=0;
		POOLTAB.add(0);
	}

	public static void main(String[] args) {
		PassOne one=new PassOne();
		try
		{
			one.parseFile();
		}
		catch (Exception e) {
			System.out.println("Error: "+e);// TODO: handle exception
		}
	}
	public void parseFile() 
	{String prev="";
	String line,code;
	try{
		br = new BufferedReader(new FileReader("/home/pmpardeshi/Desktop/try/a1_sirs_prog_ok/a1/input.asm"));
		BufferedWriter bw=new BufferedWriter(new FileWriter("/home/pmpardeshi/Desktop/try/a1_sirs_prog_ok/a1/IC.txt"));
		BufferedWriter test=new BufferedWriter(new FileWriter("/home/pmpardeshi/Desktop/try/a1_sirs_prog_ok/a1/test.txt"));
		INSTtable lookup=new INSTtable();

		while((line=br.readLine())!=null)
		{
			String parts[]=line.split("\\s+");

			if(!parts[0].isEmpty()) //processing of label
			{
				if(SYMTAB.containsKey(parts[0])){
					SYMTAB.put(parts[0], new TableRow(parts[0], lc, SYMTAB.get(parts[0]).getIndex()));

				}
				else{
					SYMTAB.put(parts[0],new TableRow(parts[0], lc, ++symIndex));
				}

				code="(S,"+SYMTAB.get(parts[0]).getIndex()+")\t";
				bw.write(code);
			}

			if(parts[1].equals("LTORG"))
			{
				lc--;
				int ptr=POOLTAB.get(pooltab_ptr);
				for(int j=ptr;j<libtab_ptr;j++)
				{
					++lc;
					LITTAB.set(j, new TableRow(LITTAB.get(j).getSymbol(),lc));
					code="(AD,05)\t(DL,02)\t(C,"+LITTAB.get(j).symbol+")";
					bw.write(code+"\n");
				}
				pooltab_ptr++;
				POOLTAB.add(libtab_ptr);
				lc++;
			}
			if(parts[1].equals("START"))
			{
				lc=expr(parts[2]);
				test.write("value of lc start"+lc);
				code="(AD,01)\t(C,"+lc+")";
				bw.write(code+"\n");
				prev="START";
			}
			else if(parts[1].equals("ORIGIN"))
			{
				lc=expr(parts[2]);


				code="(AD,03)\t(C,"+lc+")";
				bw.write(code+"\n");
			}

			//Now for EQU
			if(parts[1].equals("EQU"))
			{
				int loc1=expr(parts[2]);
				//below If conditions are optional as no IC is generated for them
				// if(parts[2].contains("+"))
				// {
				// 	String splits[]=parts[2].split("\\+");
				// 	code="(AD,04)\t(S,"+SYMTAB.get(splits[0]).getIndex()+")+"+Integer.parseInt(splits[1]);

				// }
				// else if(parts[2].contains("-"))
				// {
				// 	String splits[]=parts[2].split("\\-");
				// 	code="(AD,04)\t(S,"+SYMTAB.get(splits[0]).getIndex()+")-"+Integer.parseInt(splits[1]);
				// }
				// else
				// {
				// 	code="(AD,04)\t(C,"+Integer.parseInt(parts[2]+")");
				// }
				code="(AD,04)\t(S,"+loc1+")";

				bw.write(code+"\n");
				if(SYMTAB.containsKey(parts[0]))
					SYMTAB.put(parts[0], new TableRow(parts[0],loc1,SYMTAB.get(parts[0]).getIndex())) ;
				else
					SYMTAB.put(parts[0], new TableRow(parts[0],loc1,++symIndex));	 
			}

			if(parts[1].equals("DC"))
			{
				lc++;
				int size=Integer.parseInt(parts[2].replace("'",""));
				code="(DL,02)\t(C,"+size+")";
				bw.write(code+"\n");
			}
			else if(parts[1].equals("DS"))
			{
				
				int size=Integer.parseInt(parts[2].replace("'", ""));

				code="(DL,01)\t(C,"+size+")";
				bw.write(code+"\n");
				/*if(prev.equals("START"))
				{
					lc=lc+size-1;//System.out.println("here");
					
				}
				else
*/					lc=lc+size;
				prev="";
			}
			if(lookup.getType(parts[1]).equals("IS"))
			{
				code="(IS,0"+lookup.getCode(parts[1])+")\t";
				int j=2;
				String code2="";
				String type =parts[1];
				
				if(type.equals("STOP"))
				{

				}
				else if(type.equals("PRINT")||type.equals("READ")){


					if(SYMTAB.containsKey(parts[2])){
						SYMTAB.put(parts[2], new TableRow(parts[2], lc, SYMTAB.get(parts[2]).getIndex()));

					}
					else{
						SYMTAB.put(parts[2],new TableRow(parts[2], lc, ++symIndex));
					}

					code2+="(S,"+SYMTAB.get(parts[2]).getIndex()+")\t";
				}
				else {
					String[]pramod=parts[2].split("\\s*,\\s*");
					if(lookup.getType(pramod[0]).equals("RG"))
					{
						code2+="(RG,"+lookup.getCode(pramod[0])+")\t";
					}
					
					if(pramod[1].contains("="))
					{
						pramod[1]=pramod[1].replace("=", "").replace("'", "");
						LITTAB.add(new TableRow(pramod[1], -1,++litIndex));
						libtab_ptr++;
						code2+="(L,"+(litIndex)+")";
					}
					else if(SYMTAB.containsKey(pramod[1]))
					{
						int ind=SYMTAB.get(pramod[1]).getIndex();
						code2+= "(S,"+ind+")"; 
					}
					else
					{       
						test.write("before insertion in tble");

						SYMTAB.put(pramod[1], new TableRow(pramod[1],-1,++symIndex));
						int ind=SYMTAB.get(pramod[1]).getIndex();
						code2+= "(S,"+ind+")";
					}
				}	

				test.write("value of lc mover"+lc);
				
				
				++lc;
				code=code+code2;
				bw.write(code+"\n");
			}


			if(parts[1].equals("END"))
			{
				int ptr=POOLTAB.get(pooltab_ptr);
				for(int j=ptr;j<libtab_ptr;j++)
				{
					lc++;
					LITTAB.set(j, new TableRow(LITTAB.get(j).getSymbol(),lc));
					code="(DL,01)\t(C,"+LITTAB.get(j).symbol+")";
					bw.write(code+"\n");
				}
				pooltab_ptr++;
				POOLTAB.add(libtab_ptr);
				code="(AD,02)";
				bw.write(code+"\n");
			}

		}
		bw.close();
		test.close();
		printSYMTAB();
		//Printing Literal table
		PrintLITTAB();
		printPOOLTAB();
	}
	catch(Exception e){
		System.out.println(e.getStackTrace()[0].getLineNumber());
	}
}
void PrintLITTAB() throws IOException
{
	BufferedWriter bw=new BufferedWriter(new FileWriter("LITTAB.txt"));
	System.out.println("\nLiteral Table\n");
		//Processing LITTAB
	for(int i=0;i<LITTAB.size();i++)
	{
		TableRow row=LITTAB.get(i);
		System.out.println(i+"\t"+row.getSymbol()+"\t"+row.getAddess());
		bw.write((i+1)+"\t"+row.getSymbol()+"\t"+row.getAddess()+"\n");
	}
	bw.close();
}
void printPOOLTAB() throws IOException
{
	BufferedWriter bw=new BufferedWriter(new FileWriter("POOLTAB.txt"));
	System.out.println("\nPOOLTAB");
	System.out.println("Index\t#first");
	for (int i = 0; i < POOLTAB.size(); i++) {
		System.out.println(i+"\t"+POOLTAB.get(i));
		bw.write((i+1)+"\t"+POOLTAB.get(i)+"\n");
	}
	bw.close();
}
void printSYMTAB() throws IOException
{
	BufferedWriter bw=new BufferedWriter(new FileWriter("SYMTAB.txt"));
		//Printing Symbol Table
	java.util.Iterator<String> iterator = SYMTAB.keySet().iterator();
	System.out.println("SYMBOL TABLE");
	while (iterator.hasNext()) {
		String key = iterator.next().toString();
		TableRow value = SYMTAB.get(key);

		System.out.println(value.getIndex()+"\t" + value.getSymbol()+"\t"+value.getAddess());
		bw.write(value.getIndex()+"\t" + value.getSymbol()+"\t"+value.getAddess()+"\n");
	}
	bw.close();
}
public int expr(String str)
{
	int temp=0;
	if(str.contains("+"))
	{
		String splits[]=str.split("[+]");
		temp=SYMTAB.get(splits[0]).getAddess()+Integer.parseInt(splits[1]);
	}
	else if(str.contains("-"))
	{
		String splits[]=str.split("[-]");
		temp=SYMTAB.get(splits[0]).getAddess()-(Integer.parseInt(splits[1]));
	}
	else
	{
		temp=Integer.parseInt(str);
	}
	return temp;
}
}