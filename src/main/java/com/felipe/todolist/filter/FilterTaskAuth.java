package com.felipe.todolist.filter;

import java.io.IOException;
import java.util.Base64;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.felipe.todolist.user.InterfaceUserRepository;

import at.favre.lib.crypto.bcrypt.BCrypt;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;


@Component // tag que permite que o spring gerencie a classe, obs: a classe RestController extends Component
public class FilterTaskAuth extends OncePerRequestFilter{

    @Autowired
    private InterfaceUserRepository userRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)//filterChain é responsavel por repassar o request e response para a proxima camada
        throws ServletException, IOException {

            System.out.println("Chegou no filter");
            var serveletPath = request.getServletPath();
            System.out.println(">>>>"+serveletPath);
            if(serveletPath.startsWith("/tasks/")){//verificando se o PATH começa com: '/tasks/'

                System.out.println("Entrou no filter");
                //pegando os campos de autorização do cabeçãlho da requisição
                var authorization = request.getHeader("Authorization");
                //authorization está vindo codificada em Basic64, o corpo da mensagem vem como ex: "Basic ZSF23533sdfg43FreVgeFE="
                //A segunda parte da mensagem 'ZSF23533sdfg43FreVgeFE=' é login e senha do usuario criptografados
    
                //separando a mensagem de authorization
                authorization = authorization.substring("Basic".length());//removendo os 5 caracteres iniciais da string, que é 'Basic'
                var authEncoded = authorization.trim();//removendo os espaços que a string contem, agora temos apenas a informaçaõ criptografada
                System.out.println("Funcionou ate aqui");

                byte[] authDecode = Base64.getDecoder().decode(authEncoded);// decodificando mensagem em um array de bytes
                //falta converter a informação para String para que seja legivel
                System.out.println("passou do decode");
                
                var authString = new String(authDecode);
    
                //authString = "felipe@costa:12345" => "UserName:Senha"
                String[] credentials = authString.split(":");
                String userName = credentials[0];
                String password = credentials[1];
                
                System.out.println("credenciais>> " +userName + password);
                //montando o objeto user utilizando o metodo da interface
                var user = this.userRepository.findByUserName(userName);
    
                if(user == null){
                    response.sendError(401);//401 - Usuário sem autorização
                }else{//verificando a senha
                    //usando o metodo de verificação da classe BCrypt para saber se a senha passada corresponde com a criptografada salva no banco
                    var passwordVerify = BCrypt.verifyer().verify(password.toCharArray(),user.getPassword());
                    if(passwordVerify.verified){
                        request.setAttribute("idUser",user.getId());//passando o id do usuário pela requisição para que ele possa ser colocado nos dados da Task
                        filterChain.doFilter(request,response);//responsavel por pasar a requisição pra frente
                    }else{
                        response.sendError(401);//401 - Usuário sem autorização
                    }
                }
            }else{
                filterChain.doFilter(request,response);//responsavel por pasar a requisição pra frente

            }
            

    }
    
}
