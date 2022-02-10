package cs305.xml;

public class SqlTag {
    private String query;
    private String paramType;

    public SqlTag(String query, String paramType) {
        this.query = query;
        this.paramType = paramType;
    }

    public String getQuery() {
        return this.query;
    }

    public String getParamType() {
        return this.paramType;
    }
}
