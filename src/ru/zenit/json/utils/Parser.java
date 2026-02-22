package ru.zenit.json.utils;
import java.awt.List;
import java.util.ArrayList;
import ru.zenit.json.utils.Parser.TypesElement;
// обязаность разбить JSON на лексемы
public class Parser {
    private ArrayList<String> value_element      =  new ArrayList();
    private ArrayList<TypesElement> type_element =  new ArrayList();    
    private ArrayList<TypesElemMassiv> elements_massiva           = new ArrayList();   
    private ArrayList<TInfoRepeatingElement> InfoRepeatingElement = new ArrayList();
    
    private String jsonText;
    int index = 0;
    int countElements;
    int FindObject = 0;

    public ArrayList<String> getValue_element() {return value_element;}
    public void setValue_element(ArrayList<String> value_element) {this.value_element = value_element;}
    
    public ArrayList<TypesElement> getType_element() {return type_element;}
    public void setType_element(ArrayList<TypesElement> type_element) {this.type_element = type_element;}
    
    public String getJsonText() {return jsonText;}
    public void setJsonText(String jsonText) {this.jsonText = jsonText;}
    
    // ОПИСАНИЕ ЭЛЕМЕНТОВ JSON
    public enum TypesElement{
        CURLY_BRACKET_LEFT,
        CURLY_BRACKET_RIGHT,
        QUOTATION_MARKS,
        SQUARE_BRACKET_LEFT,
        SQUARE_BRACKET_RIGHT,
        COLON_LEFT,
        COLON_RIGHT,
        ELEMENT_NAME,
        VALUE,
        OBJECT_NAME,
        COMMA,
        MASSIV,
        VALUE_NUMBER
    }
    
    // ОПИСАНИЕ ПОВТОРЯЮЩИХСЯ ЭЛЕМЕНТОВ МАССИВА
    public enum TypesElemMassiv{
        OPEN_MASSIVE,
        CUT_OBJECT,
        CLOSE_MASSIVE
    }
    
    // запись информации структуру повторяющихся объектов массива, для удаления повторений
    private class TInfoRepeatingElement{        
        public TypesElemMassiv type;
        public int index;
        public int SQUARE;
        public int CURLY;  
        
        public String toString(){
        	String ss = "index = " + index + ",   type = " + type  + "   ,CURLY = " + CURLY;
        	return ss;
        }
    }
    
    class TCut{
        public int indxBegin = 0;
        public int indxEnd   = 0;
        public int SQUARE    = 0;
    }    
    
    public Parser(String str) {
        setJsonText(str);    	
        prepareJsonForParsing();

        index=0;
    }
    
    // удаление пробелов в JSON только между элементами, сохраняя пробелы в текстовых полях
    private void prepareJsonForParsing(){
    	jsonText = jsonText.replaceAll("\n|\r\n", "");
    	jsonText = jsonText.replaceAll("\t","");  
    	
    	int count_colon = 0;
    	boolean colon_between = false;
    	
    	StringBuilder sb = new StringBuilder();
        for (int i = 0; i < jsonText.length();i++){            
            char ss = jsonText.charAt(i);
            if (ss == '"'){ 
                count_colon = count_colon + 1;
                if (count_colon%2 == 1){
                    colon_between = true;                	
                }else{
                	colon_between = false;     
                }
            }            
            if (ss != ' '){ 
            	sb.append(Character.toString(ss));
            }else{
	            if (colon_between){
	            	sb.append(Character.toString(ss));
	            }
            }
        }
        
        
        jsonText = sb.toString();
        System.out.println("    line =    " + jsonText);
        
    }
    
    private void AddRecords(String value, TypesElement t_e) {
        value_element.add(value);
        type_element.add(t_e);
        ++index;
    }
    
    public void print() {                
        System.out.println("");
        for(int i =0; i< value_element.size(); i++) {
            System.out.println(i + "   " + type_element.get(i) + " = " + value_element.get(i));
            
        }
    }
    
    private void find() {
        int n = 0;
        System.out.println("=====================================================================================");
        for(int i=0; i<value_element.size();i++) {
            if (type_element.get(i)== TypesElement.VALUE) {
                n = i;
                if ((n+4)<value_element.size()) {
                    if (value_element.get(n+2).equals(":") && value_element.get(n+3).equals("\"")) {
                        type_element.set(n, TypesElement.ELEMENT_NAME);
                    }
                    if (value_element.get(n+2).equals(":") && value_element.get(n+3).equals("{")) {
                        type_element.set(n, TypesElement.OBJECT_NAME);
                    }    
                    if (value_element.get(n+2).equals(":") && value_element.get(n+3).equals("[")) {
                        type_element.set(n, TypesElement.MASSIV);
                    }                        
                }
                //System.out.println("        VALUE=" + i);
            }
        }        
    }
    
    public void parse_json(){
        int count_colon = 0;
        int b = 0;
        boolean colon_between = true;
        String text = ""; 
        
        
        //System.out.println("=================================");
        
        for (int i = 0; i < jsonText.length();i++){            
            char ss = jsonText.charAt(i);            
            if (jsonText.charAt(i) == '"' && jsonText.charAt(i-1)!='\\'){ 
                count_colon = count_colon + 1;
                if (count_colon%2 == 1)
                { 
                    AddRecords(Character.toString(jsonText.charAt(i)), TypesElement.COLON_LEFT);
                    b = i;
                    colon_between = false;
                }
                if (count_colon%2 == 0){
                    colon_between = true;
                    String v = jsonText.substring(b+1, i);
                    AddRecords(v, TypesElement.VALUE);
                    AddRecords(Character.toString(jsonText.charAt(i)), TypesElement.COLON_RIGHT);
                }
            }    
            if (colon_between)
            {    
                if (jsonText.charAt(i) == '['){
                    //System.out.println("SQUARE_BRACKET_LEFT = " + jsonText.charAt(i));
                    AddRecords(Character.toString(jsonText.charAt(i)), TypesElement.SQUARE_BRACKET_LEFT);
                }                    
                if (jsonText.charAt(i) == '{'){
                    //System.out.println("CURLY_BRACKET_LEFT = " + jsonText.charAt(i));
                    AddRecords(Character.toString(jsonText.charAt(i)), TypesElement.CURLY_BRACKET_LEFT);
                }
                if (jsonText.charAt(i) == '}'){
                    AddRecords(Character.toString(jsonText.charAt(i)), TypesElement.CURLY_BRACKET_RIGHT);
                }
                if (jsonText.charAt(i) == ']'){
                    if (i>0){
                        if ((jsonText.charAt(i-1) == '[')){
                            AddRecords(Character.toString('"'), TypesElement.COLON_LEFT);
                            AddRecords(Character.toString('a'), TypesElement.VALUE);
                            AddRecords(Character.toString('"'), TypesElement.COLON_RIGHT);
                        }        
                    }
                    AddRecords(Character.toString(jsonText.charAt(i)), TypesElement.SQUARE_BRACKET_RIGHT);
                }   
                
                if ((jsonText.charAt(i) == '[') && (Character.isDigit(jsonText.charAt(i+1)) ||  (jsonText.charAt(i+1) == '-') )){
                    if (Character.isDigit(jsonText.charAt(i+1)) || ((jsonText.charAt(i+1) == '-') && Character.isDigit(jsonText.charAt(i+2)))) {
                        int e=i+1;
                        while (Character.isDigit(jsonText.charAt(e)) || (jsonText.charAt(e) == '-') || (jsonText.charAt(e) == '.')) {
                            e = e + 1;
                        }
                        String v = jsonText.substring(i+1, e);
                        AddRecords("\"", TypesElement.COLON_LEFT);
                        AddRecords(v, TypesElement.VALUE_NUMBER);
                        AddRecords("\"", TypesElement.COLON_RIGHT);                        
                    }                    
                }    
                
                if (jsonText.charAt(i) == ':'){
                    AddRecords(Character.toString(jsonText.charAt(i)), TypesElement.QUOTATION_MARKS);
                    if (Character.isDigit(jsonText.charAt(i+1)) || ((jsonText.charAt(i+1) == '-') && Character.isDigit(jsonText.charAt(i+2)))) {
                        int e=i+1;
                        while (Character.isDigit(jsonText.charAt(e)) || (jsonText.charAt(e) == '-') || (jsonText.charAt(e) == '.')) {
                            e = e + 1;
                        }
                        String v = jsonText.substring(i+1, e);
                        AddRecords("\"", TypesElement.COLON_LEFT);
                        AddRecords(v, TypesElement.VALUE_NUMBER);
                        AddRecords("\"", TypesElement.COLON_RIGHT);                        
                    }
                    
                    if (
                            (jsonText.charAt(i+1)=='n' || jsonText.charAt(i+1)=='N') && 
                            (jsonText.charAt(i+2)=='u' || jsonText.charAt(i+2)=='U') &&
                            (jsonText.charAt(i+3)=='l' || jsonText.charAt(i+3)=='L') &&
                            (jsonText.charAt(i+4)=='l' || jsonText.charAt(i+4)=='L') 
                        ) 
                    {
                        AddRecords("\"", TypesElement.COLON_LEFT);
                        AddRecords("null", TypesElement.VALUE);
                        AddRecords("\"", TypesElement.COLON_RIGHT);     
                        i=i+4;
                    }
                    if (
                            (jsonText.charAt(i+1)=='t' || jsonText.charAt(i+1)=='T') && 
                            (jsonText.charAt(i+2)=='r' || jsonText.charAt(i+2)=='R') &&
                            (jsonText.charAt(i+3)=='u' || jsonText.charAt(i+3)=='U') &&
                            (jsonText.charAt(i+4)=='e' || jsonText.charAt(i+4)=='E') 
                        ) 
                    {
                        AddRecords("\"", TypesElement.COLON_LEFT);
                        AddRecords("true", TypesElement.VALUE);
                        AddRecords("\"", TypesElement.COLON_RIGHT);     
                        i=i+4;
                    }                    
                    if (
                            (jsonText.charAt(i+1)=='f' || jsonText.charAt(i+1)=='F') && 
                            (jsonText.charAt(i+2)=='a' || jsonText.charAt(i+2)=='A') &&
                            (jsonText.charAt(i+3)=='l' || jsonText.charAt(i+3)=='L') &&
                            (jsonText.charAt(i+4)=='s' || jsonText.charAt(i+4)=='S') &&
                            (jsonText.charAt(i+5)=='e' || jsonText.charAt(i+4)=='E') 
                        ) 
                    {
                        AddRecords("\"", TypesElement.COLON_LEFT);
                        AddRecords("false", TypesElement.VALUE);
                        AddRecords("\"", TypesElement.COLON_RIGHT);     
                        i=i+5;
                    }                    
                }    
                if (jsonText.charAt(i) == ','){
                    //System.out.println("COMMA = " + jsonText.charAt(i));
                    AddRecords(Character.toString(jsonText.charAt(i)), TypesElement.COMMA);
                }                        
            }    
        }
        find();
         //print();
    }
    
    public boolean verifyJSON() {
        boolean res = false;
        int square_left  = 0;
        int square_right = 0;
        //TypesElement.COLON_RIGHT
        int count_colon_ = 0;         
        
        
        int curly_left = 0;
        int curly_right = 0;
        for (int i = 0; i < jsonText.length();i++) {
            if (jsonText.charAt(i) == ']') {
                square_right++;
            }
            if (jsonText.charAt(i) == '[') {
                square_left++;
            }   
            
            if (jsonText.charAt(i) == '}') {
                curly_right++;
            }
            if (jsonText.charAt(i) == '{') {
                curly_left++;
            }
            if (jsonText.charAt(i) == '"') {
                count_colon_++;
            } 
            
            
        }
        if ((square_right==square_left) && (curly_left==curly_right) && (count_colon_%2 == 0)) {
            res = true;
        }
        return res;
    }
    
    
    
    public String CreateElement(String name, String value) 
    {
        String str="String \""+name + "\"=\"" + value + "\"" + ";";        
        return str;
    }    
    
    public String CreateObject() 
    {
        String str="";        
        return str;
    }
    
    
    //===================================================================================================================================================
    //                                    ПРИВЕТ ВСЕМ РАБОЧИЙ ВАРИАНТ
    //===================================================================================================================================================
    ///////////////////////////////////////////////////
    //Очищение от повторения объектов в массивах
    ///////////////////////////////////////////////////
    public void clearRecordsMassiv(int level, int index, String probel, ArrayList<String> value_element_p, ArrayList<TypesElement> type_element_p){
        boolean b = true;
        int n = 0;
        ++FindObject;
        
        ArrayList<TCut> CutMassiv = new ArrayList();  
        
        int find_next_ind  = 0;
        int find_first_ind = 0; 
        int lev            = level;
        int ind            = 0;        
     
        int count_SQUARE_BRACKET_LEFT  = 0;
        int count_SQUARE_BRACKET_RIGHT = 0;
        int count_CURLY_BRACKET_LEFT   = 0;
        int count_CURLY_BRACKET_RIGHT  = 0;
        
        int count_SQUARE_two = 0;
        int count_CURLY_two  = 0;
        
        boolean openScan=false;
        
        
        int v2 = 0;
        System.out.println("=====================================================================================");
        System.out.println("                        СКАНИРОВАНИЕ ПОВТОРЕНИЯ МАССИВОВ " + "level = " + level + ", FindObject = " + FindObject);
        System.out.println("=====================================================================================");
        for(int i=0; i<type_element_p.size();i++) {
            if (type_element_p.get(i)== TypesElement.SQUARE_BRACKET_LEFT) {++count_SQUARE_BRACKET_LEFT;}
            if (type_element_p.get(i)== TypesElement.SQUARE_BRACKET_RIGHT){++count_SQUARE_BRACKET_RIGHT;}                
            if (type_element_p.get(i)== TypesElement.CURLY_BRACKET_LEFT)  {++count_CURLY_BRACKET_LEFT;}                
            if (type_element_p.get(i)== TypesElement.CURLY_BRACKET_RIGHT) {++count_CURLY_BRACKET_RIGHT;}
            
            int SQUARE = count_SQUARE_BRACKET_LEFT - count_SQUARE_BRACKET_RIGHT;
            int CURLY  = count_CURLY_BRACKET_LEFT  - count_CURLY_BRACKET_RIGHT;  
            
                    if ((i+1)<type_element_p.size()){
                        if ((type_element_p.get(i)== TypesElement.SQUARE_BRACKET_LEFT) &&  (type_element_p.get(i+1)== TypesElement.CURLY_BRACKET_LEFT)) {    
                            TInfoRepeatingElement elem = new TInfoRepeatingElement();
                            elem.index  = i;
                            elem.CURLY  = CURLY;
                            elem.SQUARE = SQUARE;
                            elem.type   = TypesElemMassiv.OPEN_MASSIVE;                            
                            InfoRepeatingElement.add(elem);
                            //System.out.println(elem);
                        }                    
                    }                    
                    if ((type_element_p.get(i)== TypesElement.CURLY_BRACKET_RIGHT) && (type_element_p.size() > (i+2))) {
                        
                        if (type_element_p.get(i+1)== TypesElement.COMMA && type_element_p.get(i+2)== TypesElement.CURLY_BRACKET_LEFT){
                                TInfoRepeatingElement elem = new TInfoRepeatingElement();
                                elem.index  = i;
                                elem.CURLY  = CURLY;
                                elem.SQUARE = SQUARE;
                                elem.type   = TypesElemMassiv.CUT_OBJECT;
                                InfoRepeatingElement.add(elem);
                                //System.out.println(elem);
                        }                        
                    }                                        
                    if(type_element_p.size() > (i+1)){
                        if ((type_element_p.get(i)== TypesElement.CURLY_BRACKET_RIGHT) &&  (type_element_p.get(i+1)== TypesElement.SQUARE_BRACKET_RIGHT)) {
                            TInfoRepeatingElement elem = new TInfoRepeatingElement();
                            elem.index  = i+1;
                            elem.CURLY  = CURLY;
                            elem.SQUARE = SQUARE;
                            elem.type   = TypesElemMassiv.CLOSE_MASSIVE;    
                            InfoRepeatingElement.add(elem);
                            //System.out.println(elem);
                        }
                    }
        }
        
//        for (int k=0; k<InfoRepeatingElement.size();k++){
//        	System.out.println("(" + " i = " + k 
//               +  " ,index  = "  + InfoRepeatingElement.get(k).index        			
//        	   +  " ,InfoRepeatingElement.type  = "  + InfoRepeatingElement.get(k).type 
//               +  " ,InfoRepeatingElement.CURLY = "  + InfoRepeatingElement.get(k).CURLY 
//               +  ")");
//        }        
        System.out.println("---------------------- FindObject = " + FindObject);
        
        int nn_begin1 = -1;
        int nn_cut1   = -1;
        int nn_end1   = -1;
        int count_cut1=  0;
         
        int max_CURLY = 0;

        
        for (int j=0; j<InfoRepeatingElement.size(); j++) {
        	int p = InfoRepeatingElement.get(j).CURLY;
        	if (p>max_CURLY) max_CURLY = p;
        	if (InfoRepeatingElement.get(j).CURLY==1 || InfoRepeatingElement.get(j).CURLY==0){
                TInfoRepeatingElement m = InfoRepeatingElement.get(j);                  
                if (m.type == TypesElemMassiv.CUT_OBJECT) {
                	count_cut1 +=1;
                	if (count_cut1 == 1){
                		nn_cut1 = m.index+1;              	
                	}
                }
                if ((count_cut1>0) && (m.type == TypesElemMassiv.CLOSE_MASSIVE)){
                	count_cut1=0; 
                	nn_end1 = m.index-1;
                	
                	//System.out.println("(" + nn_cut1 + " -> " + nn_end1 + ")");
                	TCut cut = new TCut();
                	cut.indxBegin = nn_cut1;
                	cut.indxEnd   = nn_end1;
                	cut.SQUARE    = 1;
                	CutMassiv.add(cut);
                }
         	}
        }
        
        for (int k2=2; k2<=max_CURLY; k2++){
        
	        int nn_begin2  = -1;
	        int nn_cut2    = -1;
	        int nn_end2    = -1;        
	        int count_cut2 =  0;        
	        
	        System.out.println("000000000000000000000000000000000000000000000000000000");
	        for (int j=0; j<InfoRepeatingElement.size(); j++) {
	        	
	        	if (InfoRepeatingElement.get(j).CURLY==k2){
	                TInfoRepeatingElement m = InfoRepeatingElement.get(j);
	                
	                if (m.type == TypesElemMassiv.CUT_OBJECT) {
	                	count_cut2 +=1;
	                	if (count_cut2 == 1){
	                		nn_cut2 = m.index+1;              	
	                	}
	                }
	                if ((count_cut2>0) && (m.type == TypesElemMassiv.CLOSE_MASSIVE)){
	                	count_cut2=0; 
	                	nn_end2 = m.index-1;
	                	//System.out.println("(" + nn_cut2 + " -> " + nn_end2 + ")");
	                	TCut cut = new TCut();
	                	cut.indxBegin = nn_cut2;
	                	cut.indxEnd   = nn_end2;
	                	cut.SQUARE    = k2;
	                	CutMassiv.add(cut);                	
	                }
	         	}
	        } 
        }
        
        // вывод результата вычисления повторяющихся элементов в массиве в виде интервалом удаления
        for (int k=0; k<CutMassiv.size();k++){
        	System.out.println("(" + CutMassiv.get(k).indxBegin + " -> " + CutMassiv.get(k).indxEnd + ")" + "   " + CutMassiv.get(k).SQUARE);
        }
        
        // будем вырезать повторяющиеся записи в масивах
        ArrayList<String> value_element_new      = new ArrayList();
        ArrayList<TypesElement> type_element_new = new ArrayList();   
        b=true;
        for(int i=0; i<type_element_p.size();i++) {
          if (GetPoint(i,CutMassiv)){
        	  value_element_new.add(value_element_p.get(i));
        	  type_element_new.add(type_element_p.get(i));                
          }           	
        }
        
        
        value_element = value_element_new;
        type_element  = type_element_new;
        
       	System.out.println("count elevent new - " + value_element_p.size());        
        
    }

    
    private boolean GetPoint(int indx, ArrayList<TCut> cut){
    	boolean b = true;
    	
    	for(int i=0; i<cut.size(); i++){
    		if ((indx >= cut.get(i).indxBegin) && (indx <= cut.get(i).indxEnd)){
    			b=false;
    			break;
    		}
    	}
    	
    	return b;
    }
}
