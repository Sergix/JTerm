package jterm.networking.http;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;

public class HTTPRequest {

    public static class RequestBuilder {

        private String url;

        private RequestMethod requestMethod;

        private ReturnType returnType;

        private String postData;

        private String userAgent;

        private String contentType;

        private String accept;

        private boolean doOutput;

        public RequestBuilder(String url, RequestMethod requestMethod, ReturnType returnType) {
            this.url = url;
            this.requestMethod = requestMethod;
            this.returnType = returnType;
            userAgent = "Mozilla/5.0 (Windows NT 10.0; WOW64; rv:52.0) Gecko/20100101 Firefox/52.0";
        }

        public RequestBuilder postData(String postData) {
            this.postData = postData;
            return this;
        }

        public RequestBuilder userAgent(String userAgent) {
            this.userAgent = userAgent;
            return this;
        }

        public RequestBuilder contentType(String contentType) {
            this.contentType = contentType;
            return this;
        }

        public RequestBuilder accept(String accept) {
            this.accept = accept;
            return this;
        }

        public RequestBuilder doOutput(boolean doOutput) {
            this.doOutput = doOutput;
            return this;
        }

        /**
         * Builds depending of the specified return type a response.
         *
         * @return Returns the data type that is specified by the variable returnType
         */
        public <T> T build() {
            T result = null;

            try {
                HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
                conn.setRequestProperty("User-Agent", userAgent);

                switch (requestMethod) {
                    case GET:
                        conn.setRequestMethod("GET");
                        break;
                    case POST:
                        byte[] postdataBytes = postData.getBytes("UTF-8");
                        conn.setRequestMethod("POST");
                        conn.setRequestProperty("Content-Type", contentType);
                        conn.setRequestProperty("Accept", accept);
                        conn.setRequestProperty("Content-Length", String.valueOf(postdataBytes.length));
                        conn.setDoOutput(doOutput);
                        conn.getOutputStream().write(postdataBytes);
                        break;
                }

                switch (returnType) {
                    case STRING:
                        Reader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));

                        StringBuilder builder = new StringBuilder();
                        for (int i; (i = in.read()) >= 0;) {
                            builder.append((char) i);
                        }

                        result = (T) builder.toString();

                        in.close();
                        break;
                    case INPUTSTREAM:
                        result = (T) conn.getInputStream();
                        break;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            return result;
        }
    }
}
