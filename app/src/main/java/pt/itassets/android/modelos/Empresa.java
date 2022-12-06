package pt.itassets.android.modelos;

public class Empresa {
    int status;
    String message;
    Data data;

    public class Data{
        String companyName;
        int companyNIF;

        public String getCompanyNome() {
            return companyName;
        }

        public int getCompanyNIF() {
            return companyNIF;
        }
    }

    public String getMessage() {
        return message;
    }

    public int getStatus() {
        return status;
    }

    public Data getData(){
        return data;
    }
}
