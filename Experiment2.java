
package experiment;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.NoSuchElementException;
import java.util.Scanner;


public class Experiment2 {
    private static int numOfData = 0;
    private static int dimension = 0;
    private static double[][] data;
    private static double[][] copyOfdata;

     public static void main (String[] args){
         
        load("iris.txt");
        HoldOut1();
    }

    public static double[][] load(String filename) {
         numOfData=0;
        dimension = 0;
        calcDataSize(filename);  
      
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(filename)));
            data = new double[numOfData][dimension];
            for (int i = 0; i <numOfData; i++) {
                 String line = br.readLine();
                 Scanner sc = new Scanner(line).useDelimiter(" ");
               // System.out.println("");
                 for (int j = 0; j <dimension; j++) {
                    data[i][j]=sc.nextDouble();
                    //System.out.print(+data[i][j]+" ");
                 }
            }
             br.close();
        }catch (NoSuchElementException e) {
            data = null;
        }catch (FileNotFoundException e) {
            System.out.println(e);
        }catch (IOException e) {
            System.out.println(e);
        }
        return data;
    }
 
     private static void calcDataSize(String fileName) {
        BufferedReader br = null;
        try {
            br = new BufferedReader(new InputStreamReader(new FileInputStream(fileName)));
            String line = br.readLine();
            numOfData++;
            Scanner sc = new Scanner(line).useDelimiter(" ");
            while (sc.hasNext()) {
                sc.next();
                dimension++;
            }
            while ((line = br.readLine()) != null && !line.equals("")) {
                numOfData++;
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
     
    public static double[][] datacopy(String filename) {
        numOfData=0;
        dimension = 0;
        calcDataSize(filename);  
         
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(filename)));
            copyOfdata = new double[numOfData][dimension];
            // System.out.print(+dimension);
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
 
     
     public static void HoldOut1(){
        datacopy("iris.txt");
         
        int protoNum = 20;
        double[][][] proto = new double[3][protoNum][5];
	double[][][] sampledata = new double[3][50 - protoNum][5];
        
        int n = -1, protoCnt = 0, dataCnt = 0;
        
	for (int i = 0; i < copyOfdata.length; i++) {
            if (i % 50 == 0) {
		n++;
		protoCnt = 0;
		dataCnt = 0;
            }
            if (protoCnt < protoNum) {
		for (int j = 0; j <  copyOfdata[i].length; j++) {
                    proto[n][protoCnt][j] =  copyOfdata[i][j];
		}
		protoCnt++;
		} else {
                    for (int j = 0; j <  copyOfdata[i].length; j++) {
			sampledata[n][dataCnt][j] =  copyOfdata[i][j];
                    }
            dataCnt++;
            }
	}
        
        int correctNum = 0;
        
	for (int i = 0; i < sampledata.length; i++) {
            for (int j = 0; j < sampledata[i].length; j++) {
		double[] g = new double[3];

                    for (int s = 0; s < proto.length; s++) {
			g[s] = 1000000;
			for (int t = 0; t < proto[s].length; t++) {
                            double tmp = 0;
                            for (int r = 0; r < proto[s][t].length-1; r++) {
				tmp += ((sampledata[i][j][r] - proto[s][t][r])
					* (sampledata[i][j][r] - proto[s][t][r]));
                            }
                            g[s] = Math.min(tmp, g[s]);
			}
                    }
				
		double ans = Minimum(g[0], g[1], g[2]);

		if (ans == sampledata[i][j][4]){
                    correctNum++;
		}
            }
	}
        
        System.out.println("分類精度 : " + (double) correctNum / ((50-protoNum)*3) * 100 + " %");
     }
     
      public static void HoldOut2(){
        datacopy("iris.txt");
         
        int protoNum = 10;
        double[][][] proto = new double[3][protoNum][5];
	double[][][] sampledata = new double[3][50 - protoNum][5];
        
        int n = -1, protoCnt = 0, dataCnt = 0;
        
	for (int i = 0; i < copyOfdata.length; i++) {
            if (i % 50 == 0) {
		n++;
		protoCnt = 0;
		dataCnt = 0;
            }
            if (protoCnt < protoNum) {
		for (int j = 0; j <  copyOfdata[i].length; j++) {
                    proto[n][protoCnt][j] =  copyOfdata[i][j];
		}
		protoCnt++;
		} else {
                    for (int j = 0; j <  copyOfdata[i].length; j++) {
			sampledata[n][dataCnt][j] =  copyOfdata[i][j];
                    }
            dataCnt++;
            }
	}
        
        int correctNum = 0;
        
	for (int i = 0; i < proto.length; i++) {
            for (int j = 0; j < proto[i].length; j++) {
		double[] g = new double[3];

                    for (int s = 0; s < proto.length; s++) {
			g[s] = 1000000;
			for (int t = 0; t < proto[s].length; t++) {
                            double tmp = 0;
                            for (int r = 0; r < proto[s][t].length-1; r++) {
				tmp += ((proto[i][j][r] - proto[s][t][r])
					* (proto[i][j][r] - proto[s][t][r]));
                            }
                            g[s] = Math.min(tmp, g[s]);
			}
                    }
				
		double ans = Minimum(g[0], g[1], g[2]);

		if (ans == proto[i][j][4]){
                    correctNum++;
		}
            }
	}
        
        System.out.println("分類精度 : " + (double) correctNum / ((protoNum)*3) * 100 + " %");
     }
      
        
       
     
      
     
    
    
     
    private static double Minimum(double a, double b, double c) {
	if (a < b && a < c)
            return 1.0;
	else if (b < a && b < c)
            return 2.0;
	else
            return 3.0;
    }
    
}