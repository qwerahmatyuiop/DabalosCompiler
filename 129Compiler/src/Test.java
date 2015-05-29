import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;

import SyntaxAnalyzer.Parser;
import Exceptions.ParserException;
import LexicalAnalyzer.Token;
import LexicalAnalyzer.Tokenizer;


public class Test  extends JPanel{
	static File file;
	public Test() {
		// TODO Auto-generated constructor stub
	}
	public static void main(String[] args){
		JPanel jpanel = new JPanel();
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
		int result = fileChooser.showOpenDialog(jpanel);
		if (result == JFileChooser.APPROVE_OPTION) {
		     file = fileChooser.getSelectedFile();
		    System.out.println("Selected file: " + file.getAbsolutePath());
		}
		
		Tokenizer tokenizer = new Tokenizer();
		tokenizer.addExpressions();
		String code = "";
		String line = "";
		//file reading
		try{
			BufferedReader scanner = new BufferedReader(new FileReader(file));
			while((line = scanner.readLine()) != null){
				code += line;
			}
			tokenizer.tokenize(code);
		}catch(IOException io){}

		try
		{
			Parser parse = new Parser();
			parse.parse(tokenizer.getTokens());
		}
		catch (ParserException e)
		{
			System.out.println(e.getMessage());
		}
	}
}
