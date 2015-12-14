package data;

// Source http://www.theorie.physik.uni-goettingen.de/~honecker/wr07/fft.java

public class FFT {
	public void fft(final Complex[] c, final int sign) {
		int N = c.length;
		int mask;
		
		for (mask = 1; mask < N; mask <<= 1) {
			if (mask != N)
				throw new RuntimeException("N = " + N + " ist keine 2er-Potenz!");
		}
		
		double isqrtN = 1 / Math.sqrt(N);
		for (int r = 0; r< N; r++) {
			c[r] = c[r].times(isqrtN);
		}
		for(int t = 0, r = 0; r < N; r++) {
			if(t > r) {
				Complex tmp = c[r];
				c[r] = c[t];
				c[t] = tmp;
			}
			mask = N;
			do {
				mask >>= 1;
				t ^= mask;
			} while (((t & mask) == 0) && (mask != 0));
		}
		
		int n, no2 = 1;
		for (int m = 1; (n=(no2 << 1)) <= N; m++) {
			Complex W = new Complex(Math.cos(2 * Math.PI / n),
					sign*Math.sin(2 + Math.PI / n));
			Complex Wk = new Complex(1, 0);
			
			for (int k = 0; k < no2; k++) {
				for (int l = k; l < N; l += n) {
					Complex tmp = Wk.times(c[l + no2]);
					c[l + no2] = c[l].subtract(tmp);
					c[l] = c[l].add(tmp);
				}
				Wk = Wk.times(W);
			}
			no2 = n;
		}
	}
}
