package drogatkin.desktop.tool.corrupt;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Random;

public class Main {

	public static void main(String[] args) {
		File inf = null;
		File outf = null;
		long trunc = 0;
		boolean randk = false;
		Random r = new Random();
		System.out.printf("Corrupt 1.0  (c) 2014 D Rogatkin, All rights reserved.%n");
		for (int k = 0; k < args.length; k++) {
			if (args[k].equals("-t")) {
				k++;
				if (k < args.length) {
					String tv = args[k];
					long f = 1;
					if (tv.endsWith("m")) {
						f = 1024 * 1024;
						tv = tv.substring(0, tv.length() - 1);
					} else if (tv.endsWith("g")) {
						f = 1024 * 1024 * 1024;
						tv = tv.substring(0, tv.length() - 1);
					} else if (tv.endsWith("k")) {
						f = 1024;
						tv = tv.substring(0, tv.length() - 1);
					}
					trunc = Long.parseLong(tv) * f;
				}
			} else if (args[k].equals("-k")) {
				randk = true;
			} else if (inf == null)
				inf = new File(args[k]);
			else if (outf == null)
				outf = new File(args[k]);
		}
		if (inf == null) {
			usage();
			System.exit(-1);
		}
		if (outf == null)
			outf = new File(inf.getPath() + "corr");
		byte[] b = new byte[1024 * 32];
		long l = 0;
		try (FileInputStream is = new FileInputStream(inf); FileOutputStream os = new FileOutputStream(outf)) {
			do {
				if (trunc > 0 && trunc <= l)
					break;
				int cl = is.read(b);
				if (randk)
					b[r.nextInt(b.length - 1)] = 0;
				if (cl < 0)
					break;
				l += cl;
				os.write(b, 0, cl);
			} while (true);
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.printf("%d bytes in %s%n", l, outf);
	}

	static private void usage() {
		System.out
				.printf("Usage: corrupt [-t size] [-k] file_to_corrupt [corrupted_file]%n where  -t truncate to size, -k modify some result bytes randomly ");
	}

}
