package backpropagation;

import java.util.Random;

public class Backpropagation_lib2 {

	
	int in_num, h_num, h2_num, out_num, repeat;
	double alpha;
	
	//結合係数の二次元配列定義
	double[][] hw = new double[h_num][out_num];
	double[][] w = new double[h2_num][h_num];
	double[][] w2 = new double[in_num][h2_num];
	//学習で使うため
	double[] in = new double[in_num];//入力細胞用
	double[] h = new double[h_num];//中間層細胞用
	double[] h2 = new double[h2_num];//中間層細胞用
	double[] out = new double[out_num];//出力層細胞用
	double[] t = new double[out_num];//教師信号
	
	//中間層
	public Backpropagation_lib2(int in_num, int h_num, int h2_num,int out_num, int repeat, double alpha) {
		//super();
		this.in_num = in_num;
		this.h_num = h_num;
		this.h2_num = h2_num;
		this.out_num = out_num;
		this.repeat = repeat;
		this.alpha = alpha;
		//結合係数の二次元配列定義
		this.hw = new double[h_num][out_num];
		this.w = new double[h2_num][h_num];
		this.w2 = new double[in_num][h2_num];
		//学習で使うため
		this.in = new double[in_num];//入力細胞用
		this.h = new double[h_num];//中間層細胞用
		this.h2 = new double[h2_num];//中間層細胞用
		this.out = new double[out_num];//出力層細胞用
		this.t = new double[out_num];//教師信号
	}

	public void clear() {
		//結合係数の初期値
		for(int i = 0; i < in_num; i++) {
			for(int j = 0; j < h2_num; j++) {
				w2[i][j] = Math.random();
			}
		}
		for(int i = 0; i < h2_num; i++) {
			for(int j = 0; j < h_num; j++) {
				w[i][j] = Math.random();
			}
		}
		for(int i = 0; i < h_num; i++) {
			for(int j = 0; j< out_num;j++) {
				hw[i][j] = Math.random();
			}
		}
	}
	
	public void input(int teacher) {
		//指定範囲乱数発生ように定義
		Random rand = new Random();
		int temp = 0;//教師信号判断ように定義
		double temp2 = 0.0;//教師信号判断ように定義
		double sum = 0;//中間層出力総和計算用
			in[0] = 1;
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
			
	}
	
	public void output() {
		int sum;
		//上記入力に基づく、中間層の出力
		for(int j = 0; j < h2_num;j++) {
			sum = 0;
			for(int k = 0; k < in_num;k++) {
				sum += w2[k][j] * in[k];
			}
			h2[j] = 1.0 /(1 + Math.exp(-(sum)));
		}
		
		for(int j = 0; j < h_num;j++) {
			sum = 0;
			for(int k = 0; k < h2_num;k++) {
				sum += w[k][j] * h2[k];
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
	}
	
	public void err(int i) {
		//この時の誤差の計算
		double er = 0;
		for(int j = 0; j < out_num; j++) {
			er += ((double)(t[j] -out[j]) * ((double)(t[j]) - out[j]));
		}
		System.out.println("回数 : "+ i +" in = " + in[1] + "," + in[2]);
		System.out.println(" AND : " + out[0]);
		System.out.println(" OR : " + out[1]);
		System.out.println(" XOR : " + out[2]);
		System.out.println(" er : " + er);
	}

	public void out_study() {
		//誤差に基づく出力層の学習
		for(int j = 0; j < out_num; j++) {
			for(int k = 0; k <  h_num; k++) {
				double temp2 = (double)h[k] * ((double)(t[j] - out[j]) * out[j] * (1.0 - out[j]));
				hw[k][j] += alpha * temp2;
			}
		}
	}
	
	public void h_study() {
		//出力層の学習に基づく中間層の学習
		for(int j = 0;j < h_num; j++) {
			int sum = 0;
			double temp2 = 0.0;
			for(int k = 0;k < out_num; k++) {
				temp2  = (double)t[k]- (double)((t[k] - out[k]) * out[k] * (1.0 - out[k]));
				sum += temp2 * hw[j][k];
			}
			for(int k = 0; k < h2_num; k++) {
				temp2 = h2[k];
				w[k][j] += (double)alpha * sum * h[j] * (1.0 - h[j]) * temp2;
			}
		}
	}
	
	public void h2_study() {
		//出力層の学習に基づく中間層の学習
		for(int j = 0;j < h2_num; j++) {
			int sum = 0;
			double temp2 = 0.0;
			for(int k = 0;k < h_num; k++) {
				temp2 = 0.0;
				for(int l = 0;l < out_num; l++) {
					temp2  += (double)t[l]- (double)((t[l] - out[l]) * out[l] * (1.0 - out[l]));
				}
				sum += temp2 * w[j][k];
			}
			for(int k = 0; k < in_num; k++) {
				temp2 = in[k];
				w2[k][j] += (double)alpha * sum * h2[j] * (1.0 - h2[j]) * temp2;
			}
		}
	}
}
