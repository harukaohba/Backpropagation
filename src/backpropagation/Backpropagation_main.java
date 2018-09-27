package backpropagation;

import java.util.Random;
public class Backpropagation_main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		//入力層、中間層、出力層の細胞数
		int in_num = 3;
		int h_num = 3;
		int h2_num = 3;//中間層二つ目
		int out_num = 3;
		//学習繰り返し回数
		int repeat = 1000;
		//教師信号の数(その数分だけ教師信号)
		int teacher = 4;
		//学習係数
		double alpha = 0.15;
		
		// 処理前の時刻を取得
        long startTime = System.currentTimeMillis();
		//ライブラリにしようと思った。
        //中間層1つ
		/*Backpropagation_lib blib = new Backpropagation_lib(in_num, h_num, out_num, repeat,alpha);
		blib.clear();//結合係数の初期値
		//学習に入る
		for(int i = 0; i < repeat; i++) {
			blib.input(teacher);//AND,OR,XOR
			blib.output();
			blib.err(i);
			blib.out_study();
			blib.h_study();
		}*/
		//中間層2つ：計4層
		Backpropagation_lib2 blib2 = new Backpropagation_lib2(in_num, h_num, h2_num, out_num, repeat,alpha);
		blib2.clear();//結合係数の初期値
		for(int i = 0; i < repeat; i++) {
			blib2.input(teacher);//AND,OR,XOR
			blib2.output();
			blib2.err(i);
			blib2.out_study();
			blib2.h_study();
			blib2.h2_study();
		}
		
		
		// 処理後の時刻を取得
        long endTime = System.currentTimeMillis();
 
        System.out.println("開始時刻：" + startTime + " ms");
        System.out.println("終了時刻：" + endTime + " ms");
        System.out.println("処理時間：" + (endTime - startTime) + " ms");
		
		/*
		//結合係数の二次元配列定義
		double[][] hw = new double[h_num][out_num];
		double[][] w = new double[in_num][h_num];
		//学習で使うため
		double[] in = new double[in_num];//入力細胞用
		double[] h = new double[h_num];//中間層細胞用
		double[] out = new double[out_num];//出力層細胞用
		double[] t = new double[out_num];//教師信号
		*/
		
		/*
		//結合係数の初期値
		for(int i = 0; i < in_num; i++) {
			for(int j = 0; j < h_num; j++) {
				w[i][j] = Math.random();
			}
		}
		for(int i = 0; i < h_num; i++) {
			for(int j = 0; j< out_num;j++) {
				hw[i][j] = Math.random();
			}
		}
		
		//指定範囲乱数発生ように定義
		Random rand = new Random();
		int temp = 0;//教師信号判断ように定義
		double temp2 = 0.0;//教師信号判断ように定義
		double sum = 0;//中間層出力総和計算用
		//学習に入る
		for(int i = 0; i < repeat; i++) {
			in[0] = 0;
			temp = rand.nextInt(teacher);
			if(temp == 0) {
				in[1] = 0;
				in[2] = 0;
				t[0] = 0;
				t[1] = 0;
				t[2] = 0;
			}
			if(temp == 1) {
				in[1] = 1;
				in[2] = 0;
				t[0] = 0;
				t[1] = 1;
				t[2] = 1;
			}
			if(temp == 2) {
				in[1] = 0;
				in[2] = 1;
				t[0] = 0;
				t[1] = 1;
				t[2] = 1;
			}
			if(temp == 3) {
				in[1] = 1;
				in[2] = 1;
				t[0] = 1;
				t[1] = 1;
				t[2] = 0;
			}
			
			//上記入力に基づく、中間層の出力
			for(int j = 0; j < h_num;j++) {
				sum = 0;
				for(int k = 0; k < in_num;k++) {
					sum += w[k][j] * in[k];
				}
				h[j] = 1.0 /(1 + Math.exp(-(sum)));
			}
			
			//中間層の出力に基づく、出力層の出力
			for(int j = 0; j < out_num; j++) {
				sum = 0;
				for(int k = 0; k < h_num; k++) {
					sum += hw[k][j] * h[k];
				}
				out[j] = 1.0 / (1.0 + Math.exp(-(sum)));
			}
			//誤差計算用
			//double er = 0;
			//この時の誤差の計算
			er = 0;
			for(int j = 0; j < out_num; j++) {
				er += ((double)(t[j] -out[j]) * ((double)(t[j]) - out[j]));
			}
			
			//一定数で記録
			if( i % 1 == 0) {
				System.out.println("回数 : "+ i +" in = " + in[1] + "," + in[2]);
				System.out.println(" AND : " + out[0]);
				System.out.println(" OR : " + out[1]);
				System.out.println(" XOR : " + out[2]);
				System.out.println(" er : " + er);
			}
			
			
			//誤差に基づく出力層の学習
			for(int j = 0; j < out_num; j++) {
				for(int k = 0; k <  h_num; k++) {
					temp2 = (double)h[k] * ((double)(t[j] - out[j]) * out[j] * (1.0 - out[j]));
					hw[k][j] += alpha * temp2;
				}
			}
			
			//出力層の学習に基づく中間層の学習
			for(int j = 0;j < h_num; j++) {
				sum = 0;
				for(int k = 0;k < out_num; k++) {
					temp2 = (double)t[k]- (double)((t[k] - out[k]) * out[k] * (1.0 - out[k]));
					sum += temp2 * hw[j][k];
				}
				for(int k = 0; k < in_num; k++) {
					temp2 = in[k];
					w[k][j] += (double)alpha * sum * h[j] * (1.0 - h[j]) * temp2;
				}
			}
		}*/
	}

}
