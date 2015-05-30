package com.hywebchina.codebook.service;

import android.content.Context;

import com.hywebchina.codebook.db.DatabaseHelper;
import com.hywebchina.codebook.db.model.CodeLine;
import com.hywebchina.codebook.db.model.MainPassword;
import com.hywebchina.codebook.util.DesUtil;
import com.hywebchina.codebook.util.StringUtil;
import com.j256.ormlite.android.AndroidDatabaseConnection;
import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.j256.ormlite.support.DatabaseConnection;

import java.sql.SQLException;
import java.sql.Savepoint;
import java.util.Date;
import java.util.List;

/**
 * Created by Bill on 2015/5/30.
 */
public class PasswordService {
    private DatabaseHelper helper;
    private RuntimeExceptionDao<MainPassword, Integer> dao;
    private RuntimeExceptionDao<CodeLine, Integer> lineDao;

    public PasswordService(Context context) {
        this.helper = new DatabaseHelper(context);
        this.dao = helper.getMainPasswordDao();
        this.lineDao = helper.getCodeLineDao();
    }

    public void changeMainPassword(String oldPassword, String newPassword) throws Exception {
        DatabaseConnection connection = new AndroidDatabaseConnection(helper.getWritableDatabase(), true);
        Savepoint savepoint = null;
        try {
            savepoint = connection.setSavePoint("save");

            MainPassword entity = dao.queryForId(1);
            entity.setModifyDate(new Date());
            entity.setPassword(StringUtil.md5Digest(newPassword));
            dao.update(entity);

            DesUtil oldDes = new DesUtil(oldPassword);
            DesUtil newDes = new DesUtil(newPassword);
            List<CodeLine> lineList = lineDao.queryForAll();
            for(CodeLine line : lineList){
                String encryptStr = line.getPassword();//�õ����������
                String str = oldDes.decrypt(encryptStr);//�õ����������
                encryptStr = newDes.encrypt(str);//���¼��������
                line.setPassword(encryptStr);
                lineDao.update(line);
            }

            connection.commit(savepoint);
        } catch (SQLException e) {
            try {
                connection.rollback(savepoint);
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            throw new Exception("����δ�޸ĳɹ����Ѿ�rollback");
        }
    }
}
