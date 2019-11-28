package com.xforceplus.ultraman.permissions.jdbc.define;

import java.io.*;
import java.math.BigDecimal;
import java.sql.*;
import java.util.Map;

/**
 * @version 0.1 2019/11/16 23:10
 * @auth dongbin
 * @since 1.8
 */
public class ResultSetInvalidValues {

    private static final InputStream EMPTY_INPUT_STREAM = new InputStream() {
        @Override
        public int read() throws IOException {
            return -1;
        }
    };



    private ResultSetInvalidValues() {}

    public static final String STRING = "";
    public static final boolean BOOLEAN = false;
    public static final byte BYTE = 0;
    public static final byte[] BYTES = new byte[0];
    public static final short SHORT = 0;
    public static final int INT = 0;
    public static final long LONG = 0;
    public static final float FLOAT = 0.0f;
    public static final double DOUBLE = 0.0f;
    public static final BigDecimal BIG_DECIMAL = BigDecimal.ZERO;
    public static final Date DATE = new Date(Long.MIN_VALUE);
    public static final Time TIME = new Time(Long.MIN_VALUE);
    public static final Timestamp TIMESTAMP = new Timestamp(Long.MIN_VALUE);
    public static final InputStream STREAM = EMPTY_INPUT_STREAM;
    public static final Reader READER = new Reader() {

        @Override
        public int read(char[] cbuf, int off, int len) throws IOException {
            return -1;
        }

        @Override
        public void close() throws IOException {

        }
    };

    public static final Blob BLOB = new Blob() {

        @Override
        public long length() throws SQLException {
            return 0;
        }

        @Override
        public byte[] getBytes(long pos, int length) throws SQLException {
            return new byte[0];
        }

        @Override
        public InputStream getBinaryStream() throws SQLException {
            return EMPTY_INPUT_STREAM;
        }

        @Override
        public long position(byte[] pattern, long start) throws SQLException {
            return 0;
        }

        @Override
        public long position(Blob pattern, long start) throws SQLException {
            return 0;
        }

        @Override
        public int setBytes(long pos, byte[] bytes) throws SQLException {
            return 0;
        }

        @Override
        public int setBytes(long pos, byte[] bytes, int offset, int len) throws SQLException {
            return 0;
        }

        @Override
        public OutputStream setBinaryStream(long pos) throws SQLException {
            return new OutputStream() {
                @Override
                public void write(int b) throws IOException {
                }
            };
        }

        @Override
        public void truncate(long len) throws SQLException {

        }

        @Override
        public void free() throws SQLException {

        }

        @Override
        public InputStream getBinaryStream(long pos, long length) throws SQLException {
            return STREAM;
        }
    };

    public static final Clob CLOB = new Clob() {

        @Override
        public long length() throws SQLException {
            return 0;
        }

        @Override
        public String getSubString(long pos, int length) throws SQLException {
            return STRING;
        }

        @Override
        public Reader getCharacterStream() throws SQLException {
            return READER;
        }

        @Override
        public InputStream getAsciiStream() throws SQLException {
            return STREAM;
        }

        @Override
        public long position(String searchstr, long start) throws SQLException {
            return 0;
        }

        @Override
        public long position(Clob searchstr, long start) throws SQLException {
            return 0;
        }

        @Override
        public int setString(long pos, String str) throws SQLException {
            return 0;
        }

        @Override
        public int setString(long pos, String str, int offset, int len) throws SQLException {
            return 0;
        }

        @Override
        public OutputStream setAsciiStream(long pos) throws SQLException {
            return new OutputStream() {
                @Override
                public void write(int b) throws IOException {
                }
            };
        }

        @Override
        public Writer setCharacterStream(long pos) throws SQLException {
            return new Writer() {
                @Override
                public void write(char[] cbuf, int off, int len) throws IOException {
                }

                @Override
                public void flush() throws IOException {
                }

                @Override
                public void close() throws IOException {
                }
            };
        }

        @Override
        public void truncate(long len) throws SQLException {

        }

        @Override
        public void free() throws SQLException {

        }

        @Override
        public Reader getCharacterStream(long pos, long length) throws SQLException {
            return READER;
        }
    };

    public static final Ref REF = new Ref() {

        @Override
        public String getBaseTypeName() throws SQLException {
            return null;
        }

        @Override
        public Object getObject(Map<String, Class<?>> map) throws SQLException {
            return null;
        }

        @Override
        public Object getObject() throws SQLException {
            return null;
        }

        @Override
        public void setObject(Object value) throws SQLException {

        }
    };


}
