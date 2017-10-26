package com.cc.custom.gallery.utils;

import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.cc.custom.gallery.model.TXImageModel;

import java.io.File;

/**
 * Created by Cheng on 17/3/24.
 */
public class TXImageUtils {
    
    private static final String TAG = "TXImageUtils";

    private static final int DEFAULT_WH = 480;
    private static final String IMAGE_FILE_PREFIX = "TX_IMAGE_";
    private static final String IMAGE_FILE_SUFFIX = ".jpg";

    public static boolean isFileValid(String path) {
        if (TextUtils.isEmpty(path)) {
            return false;
        }
        File file = new File(path);
        return isFileValid(file);
    }

    public static boolean isFileValid(File file) {
        return file != null && file.exists() && file.isFile() && file.length() > 0 && file.canRead();
    }

    /**
     * 检查图片宽高
     *
     * @param model
     */
    public static void checkImageWidthAndHeight(@NonNull TXImageModel model) {
        if (model.getHeight() == 0 || model.getWidth() == 0) {
            int[] imageSize = TXImageUtils.getImageWidthAndHeight(model.getFilePath());
            model.setWidth(imageSize[0]);
            model.setHeight(imageSize[1]);
        }
    }

    /**
     * 获取图片大小
     *
     * @param imagePath 图片路径
     */
    public static int[] getImageWidthAndHeight(String imagePath) {
        int[] res = new int[2];

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        options.inSampleSize = 1;
        BitmapFactory.decodeFile(imagePath, options);

        res[0] = options.outWidth;
        res[1] = options.outHeight;

        if (res[0] == 0) {
            res[0] = DEFAULT_WH;
        }

        if (res[1] == 0) {
            res[1] = DEFAULT_WH;
        }

        return res;
    }

    /**
     * 保存图片到相册
     * <p>
     * 0. 检查权限
     * 1. 根据URL判断本地是否有缓存
     * 2. 有缓存，直接使用。
     * 3. 没有缓存，下载图片。
     * <p>
     * @param context  上下文
     * @param url      URL
     * @param listener 监听
     */
//    public static void saveImageToGallery(@NonNull final Context context, final String url,
//        final TXBaseDataService.TXDataServiceListener listener) {
//
//        if (StringUtils.isEmpty(url)) {
//            TXLog.d(TAG, "saveImageToGallery url isEmpty");
//            if (listener != null) {
//                listener.onDataBack(TXServiceResultModel.resultWithData(TXErrorConst.ERROR_CODE_FAIL,
//                    context.getString(R.string.tx_save_failed)), null);
//            }
//            return;
//        }
//
//        TXAppPermissionUtil.requestStorage(context).subscribe(new Action1<Boolean>() {
//            @Override
//            public void call(Boolean aBoolean) {
//                if (aBoolean) {
//                    TXDownloadManager.getInstance().download(TXDownloadManager.TYPE_IMAGE, url,
//                        new TXDownloadSimpleListener() {
//                            @Override
//                            public void onDataBack(TXServiceResultModel result, TXFileModel model, Object param) {
//                                if (listener != null) {
//                                    listener.onDataBack(result, param);
//                                }
//                            }
//                        });
//                } else {
//                    TXLog.d(TAG, "saveImageToGallery no permission");
//                    if (listener != null) {
//                        listener.onDataBack(TXServiceResultModel.resultWithData(TXErrorConst.ERROR_CODE_FAIL,
//                            context.getString(R.string.tx_save_failed)), null);
//                    }
//                }
//            }
//        });
//    }

    /**
     * 保存图片到相册
     *
     * @param context  上下文
     * @param bitmap   bitmap
     * @param fileName 文件名
     */
//    public static void saveBitmapToGallery(@NonNull final Context context, final Bitmap bitmap,
//        @Nullable final String fileName, final TXBaseDataService.TXDataServiceListener listener) {
//
//        if (bitmap == null) {
//            TXLog.d(TAG, "saveBitmapToGallery bitmap null");
//            if (listener != null) {
//                listener.onDataBack(TXServiceResultModel.resultWithData(TXErrorConst.ERROR_CODE_FAIL,
//                    context.getString(R.string.tx_save_failed)), null);
//            }
//            return;
//        }
//
//        TXAppPermissionUtil.requestStorage(context).subscribe(new Action1<Boolean>() {
//            @Override
//            public void call(Boolean aBoolean) {
//                if (aBoolean) {
//                    String name = IMAGE_FILE_PREFIX + fileName + String.valueOf(System.currentTimeMillis()) + IMAGE_FILE_SUFFIX;
//                    File file = TXFileManager.getGalleryPictureFile(name, true);
//                    if (file == null) {
//                        TXLog.d(TAG, "saveBitmapToGallery file null");
//                        if (listener != null) {
//                            listener.onDataBack(TXServiceResultModel.resultWithData(TXErrorConst.ERROR_CODE_FAIL,
//                                context.getString(R.string.tx_save_failed)), null);
//                        }
//                        return;
//                    }
//
//                    FileOutputStream fos;
//                    try {
//                        fos = new FileOutputStream(file);
//                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
//                        fos.flush();
//                        fos.close();
//                        context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(file)));
//                        if (listener != null) {
//                            listener.onDataBack(TXServiceResultModel.resultWithData(TXErrorConst.ERROR_CODE_SUCCESS),
//                                null);
//                        }
//                    } catch (FileNotFoundException e) {
//                        TXLog.e(TAG, "saveBitmapToGallery FileNotFoundException e " + e.getMessage());
//                        if (listener != null) {
//                            listener.onDataBack(TXServiceResultModel.resultWithData(TXErrorConst.ERROR_CODE_FAIL,
//                                context.getString(R.string.tx_save_failed)), null);
//                        }
//                    } catch (IOException e) {
//                        TXLog.e(TAG, "saveBitmapToGallery IOException e " + e.getMessage());
//                        if (listener != null) {
//                            listener.onDataBack(TXServiceResultModel.resultWithData(TXErrorConst.ERROR_CODE_FAIL,
//                                context.getString(R.string.tx_save_failed)), null);
//                        }
//                    } catch (Exception e) {
//                        TXLog.e(TAG, "saveBitmapToGallery Exception e " + e.getMessage());
//                        if (listener != null) {
//                            listener.onDataBack(TXServiceResultModel.resultWithData(TXErrorConst.ERROR_CODE_FAIL,
//                                context.getString(R.string.tx_save_failed)), null);
//                        }
//                    }
//                } else {
//                    TXLog.d(TAG, "saveBitmapToGallery no permission");
//                    if (listener != null) {
//                        listener.onDataBack(TXServiceResultModel.resultWithData(TXErrorConst.ERROR_CODE_FAIL,
//                            context.getString(R.string.tx_save_failed)), null);
//                    }
//                }
//            }
//        });
//    }
}
