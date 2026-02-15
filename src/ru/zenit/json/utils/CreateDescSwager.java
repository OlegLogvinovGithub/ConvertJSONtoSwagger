package ru.zenit.json.utils;

import java.util.ArrayList;
import ru.zenit.json.utils.Parser.TypesElement;

// Класс - создает файл swagger 
public class CreateDescSwager {
	
	
	public int getIndxMassiv() {
		return indxMassiv;
	}

	public void setIndxMassiv(int indxMassiv) {
		this.indxMassiv = indxMassiv;
	}

	public boolean isFirst_massiv() {
		return first_massiv;
	}

	public void setFirst_massiv(boolean first_massiv) {
		this.first_massiv = first_massiv;
	}

	public boolean isFirst_object() {
		return first_object;
	}

	public void setFirst_object(boolean first_object) {
		this.first_object = first_object;
	}

	public StringBuilder sb;
	public StringBuilder sb_new1;
	public StringBuilder sb_new2;
	private int indxMassiv = 0;
	private boolean first_massiv = true;
	private boolean first_object = true;
	TypesElement old_type_element;
	
	
    public void createObject(String probel, ArrayList<String> value_element, ArrayList<TypesElement> type_element, int level) {
    	++level;
    	System.out.print("   <<start level>> - " + level);   System.out.println(" probel - " + "\"" + probel + "\"");
    	boolean first = true;
    	String otstup   = "    ";
    	int countOtstup = 0;
    	int countObject = 0;    	
    	
    	int index = 0;    	
    	while(index < value_element.size()) {
	    	//=================================================================================
	    	// 							 НАЧАЛО
	    	//=================================================================================	    		
    		if (    
    				(level==1) && 
    				(index==0) && 
    				type_element.get(index).equals(Parser.TypesElement.CURLY_BRACKET_LEFT) && 
    				first==true && 
    				(type_element.get(index+2).equals(Parser.TypesElement.ELEMENT_NAME) || type_element.get(index+2).equals(Parser.TypesElement.MASSIV))
    			){
    			old_type_element = type_element.get(index);
    			first =  false;   	
    			System.out.println(" -------------- НАЧАЛО -------------- CURLY_BRACKET_LEFT ");
    			//System.out.println("type_element.get(index+2) = " + type_element.get(index+2).toString());
	    		int otkr_kovichki=0;
	    		int zakr_kovichki=0; 

	    		int otkr_square_kovichki=0;
	    		int zakr_square_kovichki=0; 	    		
	    		
	    		++countObject;
    			++countOtstup;
    			sb.append(probel+otstup+otstup+"type: object\r\n");
    			sb.append(probel+otstup+otstup+"properties:\r\n");   
    			
	    		ArrayList<String>       value_element_new = new ArrayList(); 
	    		ArrayList<TypesElement> type_element_new  = new ArrayList();
	    		int k = 0;
	    		
	    		
	    		for (int i = 0; i < value_element.size(); i++) {
	    			
	    			if (value_element.get(i).equals("{")) {
	    				k = 1;
	    				++otkr_kovichki;
	    			}

	    			if (value_element.get(i).equals("[")) {;
	    				++otkr_square_kovichki;
	    			}
	    			if (value_element.get(i).equals("]")) {;
    					++zakr_square_kovichki;
	    			}	    			
	    			
	    			if (k==1) {
	    				value_element_new.add(value_element.get(i));
	    				type_element_new.add(type_element.get(i));
	    				index = i;
	    			}
	    			if (value_element.get(i).equals("}")) {
	    				++zakr_kovichki;
	    				if ((otkr_kovichki == zakr_kovichki) && (otkr_square_kovichki == zakr_square_kovichki)) {
		    				k = 2;
		    				break;
	    				}
	    			}	    			
	    		}
	    		
	    		System.out.println("    count(value_element_new) = " + value_element_new.size());
	    		createObject(probel+otstup+otstup, value_element_new, type_element_new, level);
	    		//createObject(probel, value_element_new, type_element_new, level);
	    		if ((index+1) <= value_element.size()-1) { 
	    			index = index + 1;    			
	    		}    			
    		}
    		
    		
	    	//=============================================================================================
	    	//                           MASSIV В НАЧАЛЕ	   
	    	//=============================================================================================
	    	if (
    				(level==1) && 
    				(index==0) && 
	    			type_element.get(index).equals(Parser.TypesElement.SQUARE_BRACKET_LEFT) &&
	    			type_element.get(index+1).equals(Parser.TypesElement.CURLY_BRACKET_LEFT)
	    		) 
	    	{
	    		old_type_element = type_element.get(index);
	    		String NameMassiv = value_element.get(index);
	    		System.out.println(" -------------- MASSIV В НАЧАЛЕ -------- " + NameMassiv);
	    		int otkr_kovichki = 0;
	    		int zakr_kovichki = 0;   
	    		
    			sb.append(probel+otstup+otstup+"type: array\r\n");     			
    			sb.append(probel+otstup+otstup+"items:\r\n"); 	
    			
    			first_massiv = false;
			    		
			    ArrayList<String>       value_element_new = new ArrayList(); 
			    ArrayList<TypesElement> type_element_new  = new ArrayList();
			    		
			    int k = 0;
			    for (int i = index; i < value_element.size(); i++) {			    			
			    	if (value_element.get(i).equals("[")) {
			    		k = 1;
			    		++otkr_kovichki;
			    	}
			    	if (k==1) {
			    		value_element_new.add(value_element.get(i));
			    		type_element_new.add(type_element.get(i));
			    		index = i;
			    	}
			    	if (value_element.get(i).equals("]")) {
			    		++zakr_kovichki;
			    		if (otkr_kovichki == zakr_kovichki) {
			    			k = 2;
				    		index = i;
				    		break;
			    		}
			    	}			    			
			    }
	    		System.out.println("    count(value_element_new) = " + value_element_new.size());			    
			    createObject(probel+otstup+otstup, value_element_new, type_element_new, level);
			    //createObject(probel, value_element_new, type_element_new, level);
	    		if ((index+1) <= value_element.size()-1) { 
	    			index = index + 1;    			
	    		}	    
	    	}    		
     		
	    	//=================================================================================
	    	// 							 OBJECT_NAME
	    	//=================================================================================	       		
    		if (type_element.get(index).equals(Parser.TypesElement.OBJECT_NAME)) {
    			old_type_element = type_element.get(index);
    			System.out.println(" -------------- OBJECT_NAME  " +  "----------- " + value_element.get(index) + ",   level = " + level);
	    		int otkr_kovichki=0;
	    		int zakr_kovichki=0; 
	    		++countObject;
    			++countOtstup;
    			sb.append(probel+otstup+value_element.get(index)+":\r\n");
    			sb.append(probel+otstup+otstup+"type: object\r\n");
    			sb.append(probel+otstup+otstup+"properties:\r\n");  
    			
//    					136   COLON_LEFT = "
//    					137   OBJECT_NAME = body
//    					138   COLON_RIGHT = "
//    					139   QUOTATION_MARKS = :
//    					140   CURLY_BRACKET_LEFT = {
//    					141   CURLY_BRACKET_RIGHT = }    			
    			
    			first_object = false;
    			
	    		ArrayList<String>       value_element_new = new ArrayList(); 
	    		ArrayList<TypesElement> type_element_new  = new ArrayList();
	    		int k = 0;
	    		for (int i = index; i < value_element.size(); i++) {	    			
	    			if (value_element.get(i).equals("{")) {
	    				k = 1;
	    				++otkr_kovichki;
	    			}
	    			if (k==1) {
	    				value_element_new.add(value_element.get(i));
	    				type_element_new.add(type_element.get(i));
	    				index = i;
	    			}
	    			if (value_element.get(i).equals("}")) {
	    				++zakr_kovichki;
	    				if (otkr_kovichki == zakr_kovichki) {
		    				k = 2;
		    				break;
	    				}
	    			}	    			
	    		}
	    		
			    if (value_element.size() > 0) {	
		    		System.out.println("    count(value_element_new) = " + value_element_new.size());
		    		if (value_element_new.size() > 2){
		    			// защита от случая "body": {} - пустого объекта
		    			createObject(probel+otstup+otstup, value_element_new, type_element_new, level);
		    		}			    	
			    }	    		
	    		
	    		if ((index+1) <= value_element.size()-1) { 
	    			index = index + 1;    			
	    		}
    		} 
    		
    		
    		
	    	//=================================================================================
	    	// 							 ELEMENT_NAME
	    	//=================================================================================	    		
	    	if (type_element.get(index).equals(Parser.TypesElement.ELEMENT_NAME)) {	
	    		old_type_element = type_element.get(index);
	    		String name = value_element.get(index);
    			String value = value_element.get(index+4);
    			
    			++countObject;
    			sb.append(probel+otstup+name+":\r\n");
    			if (isNumeric(value)){
    				sb.append(probel+otstup+otstup+"type: \"number\"\r\n");
    			}else{
    				sb.append(probel+otstup+otstup+"type: \"string\"\r\n");
    			}
    			sb.append(probel+otstup+otstup+"description: \"\"\r\n");
    			sb.append(probel+otstup+otstup+"example: \""+ value + "\"\r\n"); 
    			System.out.println(" -------------- ELEMENT_NAME " + "----------- " + name   + ",								level = " + level);
	    	}  
	    	
	    	
	    	
	    	//=================================================================================
	    	// 							 OBJECT_NAME WITHOUT NAME
	    	//=================================================================================	    	
	    	if ((type_element.get(index).equals(Parser.TypesElement.CURLY_BRACKET_LEFT)) && (type_element.get(index+2).equals(Parser.TypesElement.OBJECT_NAME))) {	    		
    			// why a can't catch a object 	    		
    			System.out.println(" -------------- OBJECT_NAME WITHOUT NAME,   " + "level = " + level + " old_type_element = " + old_type_element);
	    		int otkr_kovichki=0;
	    		int zakr_kovichki=0; 
	    		++countObject;
    			++countOtstup;
    			if (
    				    (((index-3)>=0) && (!type_element.get(index-3).equals(Parser.TypesElement.OBJECT_NAME)))  
    				    ||
    				    (first_object==true)
    				    ||
    				    (old_type_element.equals(Parser.TypesElement.SQUARE_BRACKET_LEFT))
    			   )
    			{	
	    			sb.append(probel+"type: object\r\n");
	    			sb.append(probel+"properties:\r\n");
	    			first_object = false;
    			}
	    		ArrayList<String>       value_element_new = new ArrayList(); 
	    		ArrayList<TypesElement> type_element_new  = new ArrayList();
	    		int k = 0;
	    		for (int i = index+1; i < value_element.size(); i++) {	    			
	    			if (value_element.get(i).equals(Parser.TypesElement.OBJECT_NAME)) {
	    				k = 1;
	    			}	    				    			
	    			if (value_element.get(i).equals("{")) {
	    				++otkr_kovichki;
	    			}
	    			if (k==1) {
	    				value_element_new.add(value_element.get(i));
	    				type_element_new.add(type_element.get(i));
	    				index = i;
	    			}
	    			if (value_element.get(i).equals("}")) {
	    				++zakr_kovichki;
	    				if (otkr_kovichki == zakr_kovichki) {
		    				k = 2;
		    				break;
	    				}
	    			}	    			
	    		}
	    		//System.out.println(" ======================= DOSHEL =======================");
	    		if (value_element_new.size() > 0){
		    		System.out.println("    count(value_element_new) = " + value_element_new.size());
		    		createObject(probel+otstup+otstup, value_element_new, type_element_new, level);
	    		}
	    		//createObject(probel, value_element_new, type_element_new, level);
	    		if ((index+1) <= value_element.size()-1) { 
	    			index = index + 1;    			
	    		}	    		
	    	} 	    	
	    	
	    	
	    	//=============================================================================================
	    	//                           MASSIV 	   
	    	//=============================================================================================
	    	if (type_element.get(index).equals(Parser.TypesElement.MASSIV)) {
	    		String NameMassiv = value_element.get(index);
	    		System.out.println(" -------------- MASSIV ----------------- " + NameMassiv  + ",   level = " + level);
	    		int otkr_kovichki = 0;
	    		int zakr_kovichki = 0;  
	    		
	    		int otkr_square_kovichki=0;
	    		int zakr_square_kovichki=0; 
	    		
    			sb.append(probel+otstup+NameMassiv+":\r\n");
    			sb.append(probel+otstup+otstup+"type: array\r\n");
    			//sb.append(probel+otstup+otstup+"properties:\r\n");     			
    			sb.append(probel+otstup+otstup+"items:\r\n"); 	
    			
    			first_massiv = false;
			    		
			    ArrayList<String>       value_element_new = new ArrayList(); 
			    ArrayList<TypesElement> type_element_new  = new ArrayList();
			    		
			    int k = 0;
			    for (int i = index; i < value_element.size(); i++) {			    			
			    	if (value_element.get(i).equals("[")) {
			    		k = 1;
			    		++otkr_square_kovichki;
			    	}
			    	if (k==1) {
			    		value_element_new.add(value_element.get(i));
			    		type_element_new.add(type_element.get(i));
			    		index = i;			    		
			    	}
			    	if (value_element.get(i).equals("]")) {
			    		++zakr_square_kovichki;			    		
			    		if ((otkr_square_kovichki == zakr_square_kovichki) && (otkr_kovichki == zakr_kovichki)) {
			    			k = 2;
				    		index = i;
				    		break;
			    		}
			    	}			    			
			    }
	    		System.out.println("    count(value_element_new) = " + value_element_new.size());
			    createObject(probel+otstup+otstup, value_element_new, type_element_new, level);
			    

	    		if ((index+1) <= value_element.size()-1) { 
	    			index = index + 1;    			
	    		}	    
	    	}	 
	    	
	    	
	    	//=============================================================================================
	    	//  ARRAY  END 	   
	    	//=============================================================================================	 
	    	
	    	if(type_element.get(index).equals(Parser.TypesElement.SQUARE_BRACKET_LEFT)/* && first_massiv==true*/){
	    		old_type_element = type_element.get(index);
	    		first= false;
	    		//level=level-1;
	    		System.out.print(" -------------- SQUARE_BRACKET_LEFT -----  "); 
	    		System.out.println(" ,level = " + level);
	    		System.out.println(" ,index = " + index);
	    		
	    		int otkr_kovichki = 0;
	    		int zakr_kovichki = 0;
	    		
	    		int otkr_square_kovichki=0;
	    		int zakr_square_kovichki=0; 	    		
	    		
				if (type_element.get(index+2).equals(Parser.TypesElement.VALUE) || type_element.get(index+2).equals(Parser.TypesElement.VALUE_NUMBER)){	 
	    			String value = value_element.get(index+2);
	    			
	    			if (isNumeric(value)){
	    				sb.append(probel+otstup+otstup+"type: \"number\"\r\n");
	    			}else{
	    				sb.append(probel+otstup+otstup+"type: \"string\"\r\n");
	    			}
	    			sb.append(probel+otstup+otstup+"description: \"\"\r\n");
	    			sb.append(probel+otstup+otstup+"example: \""+ value + "\"\r\n"); 	    						
				}	    		
	    		
	    		if(type_element.get(index+1).equals(Parser.TypesElement.CURLY_BRACKET_LEFT)){
	    			if ((index+3) < type_element.size()) {	    				
	    				if(type_element.get(index+3).equals(Parser.TypesElement.OBJECT_NAME)){
	    					//System.out.println("  ==================================================================== ");
	    					if ((index-3)>0 && type_element.get(index-3).equals(Parser.TypesElement.MASSIV)){
	    						System.out.println("     попал ");
	    					}
	    					else{
	    						if (first_massiv==true){
			    	    			sb.append(probel+"type: array\r\n");
			    	    			//sb.append(probel+"properties:\r\n");     			
			    	    			sb.append(probel+"items:\r\n");
	    						}
							}
	    				}else {
				    			sb.append(probel+otstup+"type: object\r\n");
				    			sb.append(probel+otstup+"properties:\r\n");
	    				}
	    				
	    			}else {
		    			sb.append(probel+"type: object\r\n");
		    			sb.append(probel+"properties:\r\n"); 	    				
	    			}
	    			
	    		}
    		

			    		
			    ArrayList<String>       value_element_new = new ArrayList(); 
			    ArrayList<TypesElement> type_element_new  = new ArrayList();
			    		
			    int k = 0;
			    for (int i = index; i < value_element.size(); i++) {
			    			
			    	if (value_element.get(i).equals("{")) {
			    		k = 1;
			    		++otkr_kovichki;
			    	}
			    	if (value_element.get(i).equals("[")) {
			    		++otkr_square_kovichki;
			    	}
			    	if (value_element.get(i).equals("]")) {
			    		++zakr_square_kovichki;
			    	}			    	
			    	
			    	if (k==1) {
			    		value_element_new.add(value_element.get(i));
			    		type_element_new.add(type_element.get(i));
			    		index = i;
			    	}
			    	if (value_element.get(i).equals("}")) {
			    		++zakr_kovichki;
			    		if ((otkr_kovichki == zakr_kovichki) && (otkr_square_kovichki == zakr_square_kovichki)){
			    			k = 2;
				    		index = i;
				    		break;
			    		}
			    	}			    			
			    }
			    if (value_element_new.size() > 0){
		    		System.out.println("    count(value_element_new) = " + value_element_new.size());
				    //createObject(probel+otstup+otstup, value_element_new, type_element_new, level);  //02.09.2020 ujl
		    		createObject(probel+otstup, value_element_new, type_element_new, level);
		    		//createObject(probel, value_element_new, type_element_new, level);
			    }
			    //createObject(probel, value_element_new, type_element_new, level);
	    		if ((index+1) <= value_element.size()-1) { 
	    			index = index + 1;    			
	    		}   		
	    	}
	    	
	    	
	    	if ((index+1) > value_element.size()-1) {
	    		System.out.println("break" + "  , end level = " +  level);           
	    		break;
	    	}
	    	index = index + 1;  
    	}
    	first_massiv = true;
    }
    
    public String toString(){
    	return sb.toString();
    }
    

    
    public boolean isNumeric(final String str) {
        if (str == null || str.length() == 0) {
            return false;
        }

        for (char c : str.toCharArray()) {
            if (!Character.isDigit(c)) {
                return false;
            }
        }

        return true;
    } 
    
    private void print(ArrayList<String> value_element, ArrayList<TypesElement> type_element){
		System.out.println(" --- print --- ");
		System.out.println(" value_element.size() == " + value_element.size());
		System.out.println(" type_element.size()  == " + type_element.size());		
		for(int i =0; i< value_element.size(); i++) {
			System.out.println(type_element.get(i) + "\t\t\t"+value_element.get(i));    	
		}
    }
    
}
