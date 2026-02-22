package ru.zenit.base;

import javax.swing.*;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Font;
import java.awt.event.*;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import ru.zenit.json.utils.CreateDescSwager;
import ru.zenit.json.utils.FileJSON;
import ru.zenit.json.utils.Parser;
import ru.zenit.utils.FileFilterExt;

public class ConvertJSONtoSwagger extends JFrame {

    private  JButton  btnFileFilter = null;
    private  JButton  btnConverter = null;   
    private  JButton  btnSaveFile  = null;
    CreateDescSwager createSwager = null;
    
    private JFileChooser fileChooser = null;
    private JTextArea area2 = null;
    private final String[][] FILTERS = {{"txt", "Файлы text (*.txt)"},{"json" , "JSON files(*.json)"}};
    
    private String fileNameJSON;
    private String fileNameSaveJSON;
    private String line;

    
    public ConvertJSONtoSwagger() {
        super("Конвертирование JSON файлов в формат Swagger");
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        // Кнопка создания диалогового окна для сохранения файла
        btnFileFilter = new JButton("Выбор JSON файла для обработки");
        btnConverter  = new JButton("Запустить конвертацию");
        btnSaveFile   = new JButton("Сохранить результат");

        // Создание экземпляра JFileChooser 
        fileChooser = new JFileChooser();
        // Подключение слушателей к кнопкам
        addFileChooserListeners();
        
        Container cont = this.getContentPane();
        cont.setLayout(new BoxLayout(cont,BoxLayout.Y_AXIS));

        // Размещение кнопок в интерфейсе
        JPanel pnlButton = new JPanel();
        pnlButton.add(btnFileFilter);
        pnlButton.add(btnConverter);
        pnlButton.add(btnSaveFile);
        
        cont.add(pnlButton);
        
//        JPanel pnlButton = new JPanel();
//        pnlButton.add(btnFileFilter);
//        
//        cont.add(pnlButton);
        

        area2 = new JTextArea(10, 40);

        area2.setText("1) Выберите с помощью кнопки: \"Выбор JSON файла для обработки\" файл формата JSON для конвертации в формат YAML для Swagger\n "
          		   +  "2) Затем выберите кнопку \"Запустить конвертацию\" \n"
        		   +  "3) Сохраните результат преобразования в файл, с помощью кнопки \"Сохранить результат\"\n"
          		   + "или результат можно скопировать кнопками ctrl-A ctrl-C \n\n"
        		   + "     Присылайте мне пожалуйста JSON файлы которые не обработываюся этой программой. А также замечания. ");
        area2.setFont(new Font("Serif", Font.BOLD, 15));        
        // Параметры переноса слов
        area2.setLineWrap(true);
        area2.setWrapStyleWord(true);

        // Добавим поля в окно
        JPanel pnlText = new JPanel();
        pnlText.setLayout(new BorderLayout());
        //area2.setLayout(pnlText.getLayout());

        pnlText.add(new JScrollPane(area2));
        cont.add(pnlText);        
        
        
        //setContentPane(contents);
        // Вывод окна на экран
        setSize(360, 110);
        area2.setSize(200, 60);        
        this.pack();
        
    	createSwager = new CreateDescSwager();
        
        setVisible(true);
    }	

	public static void main(String[] args) {
        // Локализация компонентов окна JFileChooser
        UIManager.put("FileChooser.saveButtonText", "Сохранить");
        UIManager.put("FileChooser.cancelButtonText", "Отмена");
        UIManager.put("FileChooser.fileNameLabelText", "Наименование файла");
        UIManager.put("FileChooser.filesOfTypeLabelText", "Типы файлов");
        UIManager.put("FileChooser.lookInLabelText", "Директория");
        UIManager.put("FileChooser.saveInLabelText", "Сохранить в директории");
        UIManager.put("FileChooser.folderNameLabelText", "Путь директории");
        new ConvertJSONtoSwagger();
	}
	
	private void addFileChooserListeners(){
		
		btnFileFilter.addActionListener(new ActionListener()
		{
		    public void actionPerformed(ActionEvent e)
		    {
		    	area2.setText("");
		        fileChooser.setDialogTitle("Выберите файл");
		        // Определяем фильтры типов файлов
		        for (int i = 0; i < FILTERS[0].length; i++) {
		            FileFilterExt eff = new FileFilterExt(FILTERS[i][0], FILTERS[i][1]);
		            fileChooser.addChoosableFileFilter(eff);
		        }
		        // Определение режима - только файл
		        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		        int result = fileChooser.showSaveDialog(ConvertJSONtoSwagger.this);
		        // Если файл выбран, покажем его в сообщении
		        if (result == JFileChooser.APPROVE_OPTION )
		            JOptionPane.showMessageDialog(ConvertJSONtoSwagger.this,
		                                  "Выбран файл ( " + fileChooser.getSelectedFile().toString() + " )");
		        	fileNameJSON =  fileChooser.getSelectedFile().toString();
		        	try {
						line = FileJSON.readFile(fileNameJSON);
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
						area2.setText("Ошибка открытия файла");
					}
		        	area2.setText(line);
		        }
		});	
		
		
		btnConverter.addActionListener(new ActionListener()
		{
		    public void actionPerformed(ActionEvent e)
		    {
		        	area2.setText("");
		        	Parser pr = new Parser(line);
		        	
		        	if (pr.verifyJSON()) {
		        		pr.parse_json(); 
		        		System.out.println("count elevent - " + pr.getType_element().size());
		        		int level = 0;
		        		int index = 0;
		        		String probel = "";
		        		// убрать повторяющиеся элементы в массивах и объектах
		        		pr.clearRecordsMassiv(level, index, probel, pr.getValue_element(), pr.getType_element());
		        		pr.print();
		        	}else {
		        		System.out.println("Ошибочный json");
		        		System.exit(0);
		        	}

		        	int level = 0;
		        	System.out.println("count elevent - " + pr.getType_element().size());
		        	String ss= " ";
		        	createSwager.sb = new StringBuilder();
		        	createSwager.setIndxMassiv(0);
		        	createSwager.setFirst_massiv(true);
		        	createSwager.setFirst_object(true);
		        	createSwager.createObject(ss, pr.getValue_element(), pr.getType_element(), level);
		        	System.out.println("");
		        	area2.setText(createSwager.toString());
		    }    	
		});	

		btnSaveFile.addActionListener(new ActionListener()
		{
		    public void actionPerformed(ActionEvent e)
		    {
		        fileChooser.setDialogTitle("Выберите файл для сохранения");
		        // Определяем фильтры типов файлов
		        for (int i = 0; i < FILTERS[0].length; i++) {
		            FileFilterExt eff = new FileFilterExt(FILTERS[i][0], FILTERS[i][1]);
		            fileChooser.addChoosableFileFilter(eff);
		        }
		        // Определение режима - только файл
		        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		        int result = fileChooser.showSaveDialog(ConvertJSONtoSwagger.this);
		        // Если файл выбран, покажем его в сообщении
		        if (result == JFileChooser.APPROVE_OPTION )
		            JOptionPane.showMessageDialog(ConvertJSONtoSwagger.this,
		                                  "Выбран файл ( " + fileChooser.getSelectedFile().toString() + " )");
		        	fileNameSaveJSON =  fileChooser.getSelectedFile().toString();
		        	try {
		        		FileJSON.writeFile(createSwager.toString(), fileNameSaveJSON);
						//area2.setText(createSwager.toString());
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
						area2.setText("Ошибка сохранения файла");
					}
		        }   	
		});						
	}
	
	
	

}
