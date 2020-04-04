package com.example.doctorsays;

import android.os.Bundle;
import android.os.CancellationSignal;
import android.os.ParcelFileDescriptor;
import android.print.PageRange;
import android.print.PrintAttributes;
import android.print.PrintDocumentAdapter;
import android.print.PrintDocumentInfo;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Objects;

class PdfDocumentAdapter extends PrintDocumentAdapter {

    private String path;

    PdfDocumentAdapter(String path) {
        this.path = path;
    }

    @Override
    public void onLayout(PrintAttributes oldAttributes, PrintAttributes newAttributes, CancellationSignal cancellationSignal, LayoutResultCallback layoutResultCallback, Bundle extras) {
        if (cancellationSignal.isCanceled()) {
            layoutResultCallback.onLayoutCancelled();
        } else {
            PrintDocumentInfo.Builder builder = new PrintDocumentInfo.Builder("file name");
            builder.setContentType(PrintDocumentInfo.CONTENT_TYPE_DOCUMENT)
                    .setPageCount(PrintDocumentInfo.PAGE_COUNT_UNKNOWN)
                    .build();
            layoutResultCallback.onLayoutFinished(builder.build(), newAttributes.equals(oldAttributes));
        }
    }

    @Override
    public void onWrite(PageRange[] pages, ParcelFileDescriptor destination, CancellationSignal cancellationSignal, WriteResultCallback callback) {
        InputStream in = null;
        OutputStream out = null;
        try {
            File file = new File(path);
            in = new FileInputStream(file);
            out = new FileOutputStream(destination.getFileDescriptor());

            byte[] buff = new byte[16384];
            int size;
            while ((size = in.read(buff)) >= 0 && !cancellationSignal.isCanceled()) {
                out.write(buff, 0, size);
            }
            if (cancellationSignal.isCanceled()) {
                callback.onWriteCancelled();
            } else {
                callback.onWriteFinished(new PageRange[]{PageRange.ALL_PAGES});
            }
        } catch (Exception e) {
            callback.onWriteFailed(e.getMessage());
            Log.e("DoctorSays", Objects.requireNonNull(e.getMessage()));
            e.printStackTrace();
        }
        finally {
            try {
                assert in != null;
                in.close();
                assert out != null;
                out.close();
            } catch (Exception ex) {
                Log.e("DoctorSays", ""+ex.getMessage());
            }
        }
    }
}
