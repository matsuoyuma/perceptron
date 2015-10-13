package experiment;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.util.NoSuchElementException;
import java.util.Scanner;

/**
 *
 * @author ccilab-ke112
 */
public class Experiment {
    /*グローバル変数*/
    private static int numOfData = 0;   //データの個数
    private static int dimension = 0;   //データの次元数
    private static double[][] data;     //データの配列
    private static double[][] copyOfdata;   //データのコピー
    private static double var=0;    //標準偏差
    private static double[] varbecquerel;   //標準偏差ベクトル
    private static double average=0;    //平均
    private static double[] averagebecquerel;   //平均ベクトル
    private static double[][] expantionbecquerel;   //拡張ベクトル
    private static double[] weight = {0.33,0.23,-0.38,-0.05,0.47};  //重みベクトル
    private static double step = 0; //ステップ回数
    
    /*メイン文*/
    public static void main (String[] args){
        load("iris.txt");
        load("glass.txt");
        learningtwoclasses2();
        datacopy("iris.txt");
        perceptron1();
        perceptron2();
    }
    
    /*ファイル読み込み*/
    public static double[][] load(String filename) {
        numOfData=0;
        dimension = 0;
        calcDataSize(filename);  //データサイズ読み込み
        /*ファイル読み込み*/
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(filename)));   //ファイル読み込みクラス呼び出し
            data = new double[numOfData][dimension];    //データ格納用配列宣言
            for (int i = 0; i <numOfData; i++) {
                 String line = br.readLine();   //一行ずつ読み込み
                 Scanner sc = new Scanner(line).useDelimiter(" ");  //””で区切る
               
                 for (int j = 0; j <dimension; j++) {
                    data[i][j]=sc.nextDouble();
                 }
            }
            br.close();
        }catch (NoSuchElementException e) { //読み込む行がなかったら飛ぶ
            data = null;
        }catch (FileNotFoundException e) {  //ファイルが読み込めないとき
            System.out.println(e);
        }catch (IOException e) {    //その他のエラー
            System.out.println(e);
        }
        return data;
    }
     //データコピー用　loadと同じ
    public static double[][] datacopy(String filename) {
        numOfData=0;
        dimension = 0;
        calcDataSize(filename);  
         
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(filename)));
            copyOfdata = new double[numOfData][dimension];
      
            for (int i = 0; i <numOfData; i++) {
                String line = br.readLine();
                Scanner sc = new Scanner(line).useDelimiter(" ");
                 //System.out.println("");
                for (int j = 0; j <dimension; j++) {
                    copyOfdata[i][j]=sc.nextDouble();
                    //System.out.print(+copyOfdata[i][j]+" ");
                }
            }
            br.close();
        }catch (NoSuchElementException e) {
            copyOfdata = null;
        }catch (FileNotFoundException e) {
            System.out.println(e);
        }catch (IOException e) {
            System.out.println(e);
        }
        return copyOfdata;
    }
    /*ファイルのデータサイズを計算*/
    private static void calcDataSize(String fileName) {
        BufferedReader br = null;   //初期化
        try {
            br = new BufferedReader(new InputStreamReader(new FileInputStream(fileName)));
            String line = br.readLine();    
            numOfData++;    //初期値設定
            Scanner sc = new Scanner(line).useDelimiter(" ");
            while (sc.hasNext()) {  //列の次に何かあれば
                sc.next();  //次の列に進む
                dimension++;    //次元数を増やす
            }
            while ((line = br.readLine()) != null && !line.equals("")) {    //次の行に何か書いてあれば
                numOfData++;    //行数を増やす
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            try {
                br.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }
    /*平均ベクトルと標準ベクトル計算*/
    private static void calcvar(){
        double sum=0;
        averagebecquerel = new double [4];    //平均ベクトル配列呼び出し
        //平均計算１．０クラス
        for(int i =0;i<4;i++){
            average = 0;
            for(int j=0;j<50;j++){
                sum += data[j][i];
            }
            average = sum/50;
            averagebecquerel[i] = average;
            sum = 0;
        }
       
        System.out.print("1.0クラス 平均ベクトル ");
        
        for(int h=0;h<4;h++){ 
        System.out.print(" "+averagebecquerel[h]+" ");
        }
     
        System.out.println(" ");
      
        sum=0;
        varbecquerel=new double [4];
        //標準偏差ベクトル計算　1.0クラス
        for(int i =0;i<4;i++){
            var=0;
            for(int j=0;j<50;j++){
                sum+=(data[j][i]-averagebecquerel[i])*(data[j][i]-averagebecquerel[i]);
            }
            var=Math.sqrt(sum/50);
            varbecquerel[i]=var;
            sum=0;
        }
       
        System.out.print("1.0クラス 標準偏差ベクトル ");
        for(int h=0;h<4;h++){ 
            System.out.print(" "+varbecquerel[h]+" ");
        }
      
        System.out.println(" ");
        sum=0;
        averagebecquerel=new double [4];
        for(int i =0;i<4;i++){
            var=0;
            for(int j=50;j<100;j++){
               sum+=data[j][i];
            }
            average=sum/50;
            averagebecquerel[i]=average;
            sum=0;
        }
       
        System.out.println(" ");
        System.out.print("2.0クラス 平均ベクトル ");
        
        for(int h=0;h<4;h++){ 
            System.out.print(" "+averagebecquerel[h]+" ");
        }
      
        System.out.println(" ");
      
        sum=0;
        varbecquerel=new double [4];
      
        for(int i =0;i<4;i++){
            var=0;
            for(int j=50;j<100;j++){
                sum+=(data[j][i]-averagebecquerel[i])*(data[j][i]-averagebecquerel[i]);
            }
            var=Math.sqrt(sum/50);
            varbecquerel[i]=var;
            sum=0;
        }
       
        System.out.print("2.0クラス 標準偏差ベクトル ");
        for(int h=0;h<4;h++){ 
            System.out.print(" "+varbecquerel[h]+" ");
        }
        System.out.println(" ");
      
        sum=0;
        averagebecquerel=new double [4];
         for(int i =0;i<4;i++){
            average=0;
            for(int j=100;j<150;j++){
                sum+=data[j][i];
            }
            average=sum/50;
            averagebecquerel[i]=average;
            sum=0;
        }
       
        System.out.println(" ");
        System.out.print("3.0クラス 平均ベクトル ");
        
        for(int h=0;h<4;h++){ 
            System.out.print(" "+averagebecquerel[h]+" ");
        }
        sum=0;
        varbecquerel=new double [4];
      
        for(int i =0;i<4;i++){
            var=0;
            for(int j=100;j<150;j++){
                sum+=(data[j][i]-averagebecquerel[i])*(data[j][i]-averagebecquerel[i]);
            }
            var=Math.sqrt(sum/50);
            varbecquerel[i]=var;
            sum=0;
        }
        System.out.println(" ");       
        System.out.print("3.0クラス 標準偏差ベクトル ");
        for(int h=0;h<4;h++){ 
            System.out.print(" "+varbecquerel[h]+" ");
        }
        System.out.println(" ");
    }
    
    public static void learningtwoclasses1(){
        double result=0;
        double count=0;
        datacopy("iris.txt");
        expantionbecquerel=new double [100][5];
  
        //System.out.println(copyOfdata[0][0]);
        for(int i=0;i<100;i++){
             //System.out.println("");
            for(int j=0;j<5;j++){
                if(j==0){
                    expantionbecquerel[i][j] = 1;
                }else{
                    expantionbecquerel[i][j] = copyOfdata[i][j-1];
                }
               // System.out.print(expantionbecquerel[i][j]+" ");
            }
        }
        
        for(int i=0;i<100;i++){
            // System.out.println("");
            for(int j=0;j<5;j++){
                result+=weight[j]*expantionbecquerel[i][j];
                //System.out.print(copyOfdata[i][j]+" ");
            }
            
            if(result>=0){
                copyOfdata[i][4]=1.0;
            }else{
                copyOfdata[i][4]=2.0;
            }
            //System.out.println(+result);
            result=0;
         }
         
        for(int i=0;i<100;i++){
            if(copyOfdata[i][4] == data[i][4]){
                count++;
            }     
        }
         //System.out.println(+count);
        System.out.println("問７　学習率　"+count/100*100+"%");
    }
    
    public static void perceptron1(){
        datacopy("iris.txt");
        expantionbecquerel=new double [100][5];
        double row =0.01;
        double[] weight = {0.33,0.23,-0.38,-0.05,0.47};
        double count=0;
        double result=0;
        int step=0;
        
        for(int i=0;i<100;i++){
             //System.out.println("");
            for(int j=0;j<5;j++){
                if(j==0){
                expantionbecquerel[i][j]=1;
                }else{
                    expantionbecquerel[i][j]=copyOfdata[i][j-1];
                }
               // System.out.print(expantionbecquerel[i][j]+" ");
            }
        }
        
        
        
    for(int t=0;t<10;t++){
           
        for(int i=0;i<100;i++){
             
            for(int j=0;j<5;j++){
                result+=weight[j]*expantionbecquerel[i][j];
            } 
                if(result>=0){
                    copyOfdata[i][4]=1.0;
                }else{
                 copyOfdata[i][4]=2.0;
                }
                 
                if(copyOfdata[i][4] != data[i][4]){
                    if(i<50){
                        for(int j=0;j<5;j++){
                            weight[j] += row * expantionbecquerel[i][j];
                        }
                    }else{
                        for(int j=0;j<5;j++){
                            weight[j] -= row * expantionbecquerel[i][j];
                            //System.out.print(+expantionbecquerel[i][j]+" ");
                   //System.out.println("not2.0 2.0 ");
                    //System.out.print(+i);
                        }
                    }
                }
                   // for(int j=0;j<5;j++){
               // System.out.print(+weight[j]+" ");
                   // }
                    //System.out.println(" ");
            result=0; 
        }
                    
           
    }
            //System.out.println(+result);   
    
     //}
        // for(int i=0;i<100;i++){
            // System.out.println("");
            //for(int j=0;j<5;j++){
               //result+=weight[j]*expantionbecquerel[i][j];
                //System.out.print(copyOfdata[i][j]+" ");
            //}
           // if(result>=0){
                 //   copyOfdata[i][4]=1.0;
                // }else{
                // copyOfdata[i][4]=2.0;
               // }
            //System.out.println(+result);
            //result=0;
         //}
         
         for(int i=0;i<100;i++){
             if(copyOfdata[i][4] == data[i][4]){
                 count++;
             }
        }
        
        // System.out.println(+count);
         
        for(int i=0;i<5;i++){
            System.out.print(+weight[i]+" ");
        }
        System.out.println("");
         
        System.out.println("問8　学習率　"+count/100*100+"%");
    }
    
    public static void perceptron2(){
        datacopy("iris.txt");
        expantionbecquerel=new double [150][5];
        double[] weight = {0.33,0.23,-0.38,-0.05,0.47};
        double count = 0.0;
        double row =0.01;
       
        
        for(int i=0;i<150;i++){
            //System.out.println("");
            for(int j=0;j<5;j++){
                if(j==0){
                expantionbecquerel[i][j]=1;
                }else{
                    expantionbecquerel[i][j]=copyOfdata[i][j-1];
                }
               //System.out.print(expantionbecquerel[i][j]+" ");
            }
        }
        
        for(int t=0;t<10;t++){
           
            for(int i=50;i<150;i++){
                double result = 0.0;
                double[] sampledata = { 1, copyOfdata[i][0], copyOfdata[i][1],
						copyOfdata[i][2], copyOfdata[i][3] };
                for(int j=0;j<5;j++){
                 //System.out.print(+weight[j]+" ");
                    result += weight[j] * sampledata[j];
                } 
              //System.out.println(+result);
            if(result > 0.0){
                copyOfdata[i][4]=2.0;
            }else{
                copyOfdata[i][4]=3.0;
            }
           // if(copyOfdata[i][4] == data[i][4]){
               //  count++;}
            
           if(copyOfdata[i][4] != data[i][4]){
              //System.out.print(" true");
                if(copyOfdata[i][4] == 2.0){
                    for(int j=0; j<5; j++){
                         // System.out.print(+copyOfdata[i][j]+" ");]
                        double rowx = row * sampledata[j];
                        weight[j] -= rowx;
                         
                    }
                     //System.out.print(" Virginia");
                }else if(copyOfdata[i][4]==3.0){
                    for(int j=0; j<5; j++){
                        double rowx = row * sampledata[j];
                        weight[j] += rowx;
                         
                         // System.out.print(" Versicolor");
                    }
                 
                }
            }
        }   
    }
    
        for(int i=0;i<5;i++){
            System.out.print(weight[i]+" ");
        }
    
        int correctNum = 0;
	for (int i = 50; i < 150; i++) {
				
            double result = 0;
            double[] sampledata = { 1, copyOfdata[i][0], copyOfdata[i][1], copyOfdata[i][2], copyOfdata[i][3] };
                                
            for (int j = 0; j < 5; j++) {
                result += weight[j] * sampledata[j];
            }

            if (result > 0){
                result = 2.0;
            } else if (result < 0){
                result = 3.0;
            }

            if (result == copyOfdata[i][4]) {
                correctNum++;
            }
	}
        
        System.out.println("");
        System.out.println("問9　学習率　"+correctNum+"%");
    }
    
    public static void learningtwoclasses2(){
        double result=0;
        double count=0;
        datacopy("iris.txt");
        expantionbecquerel=new double [100][5];
  
        //System.out.println(copyOfdata[0][0]);
        for(int i=0;i<100;i++){
            System.out.println("");
            for(int j=0;j<5;j++){
                if(j==0){
                    expantionbecquerel[i][j]=1;
                }else{
                    expantionbecquerel[i][j]=copyOfdata[i+50][j-1];
                }
               // System.out.print(expantionbecquerel[i][j]+" ");
            }
        }
        
         for(int i=0;i<100;i++){
            // System.out.println("");
            for(int j=0;j<5;j++){
                result+=weight[j]*expantionbecquerel[i][j];
                //System.out.print(copyOfdata[i][j]+" ");
            }
            //System.out.println(+result);
            result=0;
         }
         
         
         
        for(int i=0;i<100;i++){
            if(copyOfdata[i+50][4] == data[i+50][4]){
                count++;
            }
             
        }
         //System.out.println(+count);
        System.out.println("問9　学習率　"+count/100*100+"%");
    }
       
}

