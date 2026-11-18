import java.util.Locale;
import java.util.Scanner;

public class Prova2 {

    public static final int TAM_CONTAS = 50;
    public static final int TAM_OP = 1000;

    static Scanner sc = new Scanner(System.in);

   
    static class Conta {
        int id;
        String cliente;
        double saldo;
        double limite;
    }

    static class Operacao {
        int id;
        int idConta;
        char tipo; 
        double valor;
    }

    
    public static int buscaId(Conta[] v, int tam, int x) {
        for (int i = 0; i < tam; i++) {
            if (v[i].id == x) {
                return i;
            }
        }
        return -1;
    }

    
    public static int leInteiro(String msg) {
        System.out.print(msg);
        int x = sc.nextInt();
        sc.nextLine();
        return x;
    }

    public static String leTexto(String msg) {
        System.out.print(msg);
        return sc.nextLine();
    }

    public static double lePositivo(String msg) {
        double x;
        do {
            System.out.print(msg);
            x = sc.nextDouble();
            if (x < 0) {
                System.out.println("Valor não pode ser negativo!");
            }
        } while (x < 0);
        sc.nextLine();
        return x;
    }

   
    public static void preencheConta(Conta c, int id) {
        c.id = id;
        c.cliente = leTexto("Cliente: ");
        c.saldo = lePositivo("Saldo: ");
        c.limite = lePositivo("Limite: ");
    }

   
    public static int cadastrarConta(Conta[] v, int tam) {
        if (tam >= TAM_CONTAS) {
            System.out.println("Vetor de contas cheio!");
            return tam;
        }

        int id = leInteiro("ID: ");
        if (buscaId(v, tam, id) != -1) {
            System.out.println("Já existe uma conta com esse ID!");
            return tam;
        }

        Conta c = new Conta();
        preencheConta(c, id);
        v[tam] = c;
        return tam + 1;
    }

   
    public static int buscaBinariaCliente(Conta[] v, int tam, String x) {
        int ini = 0;
        int fim = tam - 1;

        while (ini <= fim) {
            int meio = (ini + fim) / 2;
            int cmp = v[meio].cliente.compareToIgnoreCase(x);

            if (cmp == 0) {
                return meio;
            } else if (cmp < 0) {
                ini = meio + 1;
            } else {
                fim = meio - 1;
            }
        }
        return -1;
    }

    
    public static int insertionSortCliente(Conta[] v, int tam) {
        for (int i = 1; i < tam; i++) {
            Conta chave = v[i];
            int j = i - 1;

            while (j >= 0 && v[j].cliente.compareToIgnoreCase(chave.cliente) > 0) {
                v[j + 1] = v[j];
                j--;
            }
            v[j + 1] = chave;
        }
        return tam;
    }

   
    public static int filtraOperacoes(Operacao[] vOp, int tamOp, Operacao[] vOpFiltrado, int x) {
        int tamFiltrado = 0;

        for (int i = 0; i < tamOp; i++) {
            if (vOp[i].idConta == x) {
                vOpFiltrado[tamFiltrado] = vOp[i];
                tamFiltrado++;
            }
        }
        return tamFiltrado;
    }

   

    public static double calculaSaldoAnterior(Operacao[] v, int inicio) {
        double saldo = 0;
        for (int i = 0; i < inicio; i++) {
            saldo += v[i].valor;
        }
        return saldo;
    }

    
    public static String nomeOperacao(char tipo) {
        switch (tipo) {
            case 'D':
                return "Depósito";
            case 'S':
                return "Saque";
            case 'T':
                return "Transferência";
            default:
                return "Desconhecida";
        }
    }

    
    public static String formataValor(double valor) {
        char letra = (valor >= 0) ? 'C' : 'D';
        return String.format(Locale.US, "%7.2f%c", Math.abs(valor), letra);
    }

   
    public static void imprimeCabecalho(Conta c) {
        System.out.println("+----------------------------------------+");
        System.out.printf("| C O N T A   N.%03d%n", c.id);
        System.out.println("+----------------------------------------+");
        System.out.println("|CLIENTE: " + c.cliente);
        System.out.println("+----------------------------------------+");
        System.out.println("| ID | O P E R A Ç Ã O | VALOR | SALDO |");
        System.out.println("+----+-----------------+--------+--------+");
    }

    
    public static void imprimeSaldoAnterior(double saldo) {
        System.out.println("|    |Saldo Anterior   |        |" + formataValor(saldo) + "|");
    }

   
    public static void imprimeOperacao(Operacao op, double saldo) {
        System.out.println("|" + String.format("%04d", op.id) + "|"
                + String.format("%-17s", nomeOperacao(op.tipo)) + "|"
                + formataValor(op.valor) + "|"
                + formataValor(saldo) + "|");
    }

    
    public static void imprimeRodape(double saldo) {
        System.out.println("+----+-----------------+--------+--------+");
        System.out.println("            Saldo Atual |" + formataValor(saldo) + "|");
        System.out.println("                        +--------+");
    }

    
    public static void extrato(Conta[] vContas, int tamConta, Operacao[] vOp, int tamOp) {
        int idConta = leInteiro("ID da conta: ");

        int pos = buscaId(vContas, tamConta, idConta);
        if (pos == -1) {
            System.out.println("Conta não encontrada!");
            return;
        }

        Operacao[] vFiltrado = new Operacao[TAM_OP];
        int n = filtraOperacoes(vOp, tamOp, vFiltrado, idConta);

        int inicio = Math.max(0, n - 5);
        double saldo = calculaSaldoAnterior(vFiltrado, inicio);

        imprimeCabecalho(vContas[pos]);
        imprimeSaldoAnterior(saldo);

        for (int i = inicio; i < n; i++) {
            saldo += vFiltrado[i].valor;
            imprimeOperacao(vFiltrado[i], saldo);
        }

        imprimeRodape(saldo);
    }

    public static void main(String[] args) {
        Conta[] contas = new Conta[TAM_CONTAS];
        int tamContas = 0;

        tamContas = cadastrarConta(contas, tamContas);
        tamContas = cadastrarConta(contas, tamContas);

        insertionSortCliente(contas, tamContas);
        String nome = leTexto("Cliente a buscar: ");
        System.out.println("Posição: " + buscaBinariaCliente(contas, tamContas, nome));
    }
}
