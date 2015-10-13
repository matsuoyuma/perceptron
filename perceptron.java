
package perceptron;

public class perceptron {

	static double[][] data={	//元データ
		{1.2,	1.0},
		{0.2,	1.0},
		{-0.2,	1.0},
		{-0.5,	2.0},
		{-1.0,	2.0},
		{-1.5,	2.0},
	};

	public static void main(String[] args) {

		double[][] calcdata={	//計算結果保管用データ
				{1.2,	0},
				{0.2,	0},
				{-0.2,	0},
				{-0.5,	0},
				{-1.0,	0},
				{-1.5,	0},
		};

		/*raw1.2 重みベクトルw0:-7,w1:2のとき*/
		double raw=1.2;	//rawの値
		double[] w1={-7,2};	//重みベクトルw0,w1 1回目
		calcperceptron(calcdata,raw,w1);

		/*raw2.0 重みベクトルw0:11,w1:5のとき*/
		raw=2.0;	//rawの値
		double[] w2={11,5};	//重みベクトルw0,w1　２回目
		calcperceptron(calcdata,raw,w2);

		/*raw3.6 重みベクトルw0:-7,w1:2のとき*/
		raw=3.6;	//rawの値
		double[] w3={-7,2};	//重みベクトルw0,w1　３回目
		calcperceptron(calcdata,raw,w3);

	}

	public static void calcperceptron(double[][] calcdata,double raw, double[] w){
		double result=0.0;	//識別関数の結果
		double count=0.0;	//元データと正しいかをカウント
		double per=0.0;	//正答率の百分率
		double[][] expantionbecquerel;	//拡張ベクトル
		expantionbecquerel=new double[6][2];


		/*拡張ベクトルの配置*/
		for(int i=0;i<6;i++){
			for(int j=0;j<2;j++){
				if(j==0){
					expantionbecquerel[i][j]=1;
				}else{
					expantionbecquerel[i][j]=calcdata[i][j-1];
				}
			}
		}

		while(per != 1.0){	//正答率が100%になるまで回す

			per = 0.0;	//正答率の初期化

			for(int i = 0;i<expantionbecquerel.length;i++){

				for(int j = 0;j<w.length;j++){	//g(x)=w*xで計算
					result += w[j] * expantionbecquerel[i][j];
				}

				if(result > 0){	//g(x)が正ならば
					calcdata[i][1]=1.0;	//ω1と判定
				}else{	//g(x)が負ならば
					calcdata[i][1]=2.0;	//ω2と判定
				}

				if(i<3 && calcdata[i][1]==2.0){	//もしω1をω2と判定していれば
					for(int j = 0;j<2;j++){
						w[j]=w[j]+raw*expantionbecquerel[i][j];	//重みベクトルを更新
					}
				}

				if(i >= 3 && calcdata[i][1]==1.0){	//もしω2をω1と判定していれば
					for(int j=0;j<2;j++){
						w[j]=w[j]-raw*expantionbecquerel[i][j];	//重みベクトルを更新
					}
				}

				for(int j = 0;j<w.length;j++){
					System.out.print(+w[j]+" ");
				}
				System.out.println();

				result=0;
			}

			/*認識率を出す*/
			for(int i = 0;i<calcdata.length;i++){

				for(int j = 0;j<2;j++){
					result+=w[j] * expantionbecquerel[i][j];	//更新した重みベクトルでクラス分け
				}

				if(result>0){
					calcdata[i][1]=1.0;
				}else{
					calcdata[i][1]=2.0;
				}

				result=0;
			}

			for(int i = 0;i<calcdata.length;i++){

				if(calcdata[i][1] == data[i][1]){	//計算結果と元データのクラスが正しければカウンタをプラス
					count++;
				}

			}

			per=count/calcdata.length;	//正答率の百分率を計算

			count=0.0;	//カウントを初期化
		}

		System.out.println();

	}

}
