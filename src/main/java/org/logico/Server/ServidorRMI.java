package org.logico.Server;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import org.logico.model.Candidato;
import org.logico.model.EleicaoImpl;

import java.io.FileReader;
import java.lang.reflect.Type;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.List;
import java.util.Map;

public class ServidorRMI {
    public static void main(String[] args) {
        try {

            Gson gson = new Gson();
            FileReader reader = new FileReader("C:\\Users\\edufr\\IdeaProjects\\RMi\\src\\main\\java\\org\\logico\\data.json");


            JsonObject jsonObject = JsonParser.parseReader(reader).getAsJsonObject();


            Type candidatoListType = new TypeToken<List<Candidato>>() {}.getType();
            List<Candidato> candidatos = gson.fromJson(jsonObject.get("candidatos"), candidatoListType);


            Type votosMapType = new TypeToken<Map<String, Integer>>() {}.getType();
            Map<String, Integer> votos = gson.fromJson(jsonObject.get("votos"), votosMapType);


            String caminhoArquivoCandidatos = "C:\\Users\\edufr\\IdeaProjects\\RMi\\src\\main\\java\\org\\logico\\data.json";
            EleicaoImpl eleicao = new EleicaoImpl(caminhoArquivoCandidatos, votos);


            Registry registry = LocateRegistry.createRegistry(1099);
            registry.rebind("Eleicao", eleicao);
            System.out.println("Servidor RMI pronto.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
