package com.ikould.back;

import android.os.Handler;
import android.util.Log;

import com.ikould.back.config.AppConfig;
import com.ikould.back.data.AppData;
import com.ikould.back.utils.Constants;
import com.ikould.frame.application.BaseApplication;
import com.ikould.frame.utils.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Application
 * <p>
 * Created by liudong on 2016/7/1.
 */
public class CoreApplication extends BaseApplication {
    public         Handler         killAppsHandler;
    //所有APP
    public         List <AppData>  allApps;
    //有服务正在运行的APP
    public         List <AppData>  allRunApps;
    //收藏文件中的包名
    public         List <String>   saveApps;
    private static CoreApplication instance;

    public Handler handler = new Handler();

    public static CoreApplication getInstance() {
        return instance;
    }

    @Override
    protected void onBaseCreate() {
        instance = this;
        saveApps = new ArrayList <>();
        findSavePackages();
        Log.d("liudong", saveApps.toString());
    }

    @Override
    public void crashFileSaveTo(String filePath) {

    }

  /*  @Override
    protected void onBaseCreate() {

    }

    @Override
    public void crashFileSaveTo(String filePath) {
        //no do
    }
*/

    /**
     * 从文件中获取所有保存的PackageName
     */
    public void findSavePackages() {
        String filePath = AppConfig.getInstance().OTHER_DIR + File.separator + Constants.PACKAGE_FILTER;
        StringBuilder builder = FileUtils.readFile(filePath, "utf-8");
        if (builder != null) {
            String packageStr = builder.toString().trim();
            String rex = "(\\b\\w+\\.)+\\b\\w+";
            Pattern pattern = Pattern.compile(rex);
            Matcher matcher = pattern.matcher(packageStr);
            while (matcher.find()) {
                saveApps.add(matcher.group());
            }
        }
    }

    /**
     * 保存包名到文件中去
     */
    public void savePackageToFile(String packageName, boolean isAddOr) {
        String filePath = AppConfig.getInstance().OTHER_DIR + File.separator + Constants.PACKAGE_FILTER;
        File file = new File(filePath);
        if (file.exists()) {
            file.delete();
        }
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (isAddOr) {
            saveApps.add(packageName);
        } else {
            saveApps.remove(packageName);
        }
        FileUtils.writeFile(filePath, saveApps.toString(), false);
    }
}
