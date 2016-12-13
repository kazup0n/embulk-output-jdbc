package org.embulk.output.mysql;


import com.google.common.base.Optional;
import com.mysql.jdbc.PreparedStatement;
import org.embulk.output.jdbc.BatchInsert;
import org.embulk.output.jdbc.JdbcSchema;
import org.embulk.output.jdbc.MergeConfig;
import org.embulk.output.jdbc.StandardBatchInsert;
import org.embulk.spi.Exec;
import org.embulk.spi.time.Timestamp;
import org.slf4j.Logger;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.Calendar;

public class MySQLLoadDataBatchInsert implements BatchInsert {

    private final Logger logger = Exec.getLogger(StandardBatchInsert.class);
    private StringBuilder builder = new StringBuilder();

    private final MySQLOutputConnector connector;
    private final Optional<MergeConfig> mergeConfig;
    private MySQLOutputConnection connection;
    private int batchRows;
    private int totalRows;
    private int index;
    private int batchWeight;
    private String tableName;

    public MySQLLoadDataBatchInsert(MySQLOutputConnector connector, Optional<MergeConfig> mergeConfig) throws IOException, SQLException {
        this.connector = connector;
        this.mergeConfig = mergeConfig;
    }

    @Override
    public void prepare(String loadTable, JdbcSchema insertSchema) throws SQLException {
        this.connection = this.connector.connect(true);
        this.batchRows = 0;
        this.totalRows = 0;
        this.builder.delete(0, builder.length());
        this.tableName = loadTable;
    }

    @Override
    public int getBatchWeight() {
        return batchWeight;
    }

    @Override
    public void add() throws IOException, SQLException
    {
        builder.append("\n");
        index = 1;  // PreparedStatement index begins from 1
        batchRows++;
        batchWeight += 32;  // add weight as overhead of each rows
    }

    @Override
    public void close() throws IOException, SQLException
    {
        connection.close();
    }

    @Override
    public void flush() throws IOException, SQLException
    {
        logger.info(String.format("Loading %,d rows", batchRows));
        long startTime = System.currentTimeMillis();
        final PreparedStatement stmt = connection.createPreparedStatement("load data local infile ''into table "+ tableName  + " fields terminated by ','");
        stmt.setLocalInfileInputStream(new ByteArrayInputStream(builder.toString().getBytes()));
        stmt.execute();
        stmt.close();
        double seconds = (System.currentTimeMillis() - startTime) / 1000.0;

        totalRows += batchRows;
        logger.info(String.format("> %.2f seconds (loaded %,d rows in total)", seconds, totalRows));
        this.builder.delete(0, builder.length());
        batchRows = 0;
        batchWeight = 0;
    }

    public void finish() throws IOException, SQLException
    {
        if (getBatchWeight() != 0) {
            flush();
        }
    }

    @Override
    public void setNull(int sqlType) throws IOException, SQLException
    {
        append("\\N,", 8);
    }

    public void setBoolean(boolean v) throws IOException, SQLException
    {
        append(Boolean.toString(v), 1);
    }

    public void setByte(byte v) throws IOException, SQLException
    {
        append(Byte.toString(v), 1);
    }

    public void setShort(short v) throws IOException, SQLException
    {
        append(Short.toString(v), 2);
    }

    public void setInt(int v) throws IOException, SQLException
    {
        append(Integer.toString(v), 4);
    }

    public void setLong(long v) throws IOException, SQLException
    {
        append(Long.toString(v), 8);
    }

    public void setFloat(float v) throws IOException, SQLException
    {
        append(Float.toString(v), 4);
    }

    public void setDouble(double v) throws IOException, SQLException
    {
        append(Double.toString(v), 8);
    }

    public void setBigDecimal(BigDecimal v) throws IOException, SQLException
    {
        // use estimated number of necessary bytes + 8 byte for the weight
        // assuming one place needs 4 bits. ceil(v.precision() / 2.0) + 8
        append(v.toString(), (v.precision() & ~2) / 2 + 8);
    }

    public void setString(String v) throws IOException, SQLException
    {
        // estimate all chracters use 2 bytes; almost enough for the worst case
        append(v, v.length()*2+4);
    }

    public void setNString(String v) throws IOException, SQLException
    {
        setString(v);
    }

    public void setBytes(byte[] v) throws IOException, SQLException
    {
        throw new AssertionError("byte not supported");
    }

    public void setSqlDate(Timestamp v, Calendar cal) throws IOException, SQLException
    {
        throw new AssertionError("sql date not supported");
    }

    public void setSqlTime(Timestamp v, Calendar cal) throws IOException, SQLException
    {
        throw new AssertionError("sql time not supported");
    }

    public void setSqlTimestamp(Timestamp v, Calendar cal) throws IOException, SQLException
    {
        throw new AssertionError("sql timestamp not supported");
    }

    private void append(String s, int weight){
        builder.append(s).append(",");
        nextColumn(weight);
    }

    private void nextColumn(int weight)
    {
        index++;
        batchWeight += weight + 4;  // add weight as overhead of each columns
    }


}
