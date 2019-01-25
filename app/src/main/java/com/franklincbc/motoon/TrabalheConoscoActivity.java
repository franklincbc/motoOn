package com.franklincbc.motoon;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import com.franklincbc.motoon.Utils.Constants;
import com.franklincbc.motoon.model.Usuario;

public class TrabalheConoscoActivity extends AppCompatActivity {

    TextView txtRelacContratual;
    TextView txtOsServicos;
    TextView txtUsoDoServico;
    TextView txtPagamento;
    TextView txtRecusaGarantia;
    TextView txtLegislacaoAplicavel;
    TextView txtOutrasDisposicoes;
    CheckBox chk_Aceito;
    Button btnAceito;
    Usuario mUsuario;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trabalhe_conosco);

        txtRelacContratual        = (TextView)findViewById(R.id.trabalhe_conosco_relacionamento_contratual);
        txtOsServicos             = (TextView)findViewById(R.id.trabalhe_conosco_os_servico);
        txtUsoDoServico           = (TextView)findViewById(R.id.trabalhe_conosco_uso_do_servico);
        txtPagamento              = (TextView)findViewById(R.id.trabalhe_conosco_pagamento);
        txtRecusaGarantia         = (TextView)findViewById(R.id.trabalhe_conosco_recusa_de_garantia);
        txtLegislacaoAplicavel    = (TextView)findViewById(R.id.trabalhe_conosco_legislacao_aplicavel);
        txtOutrasDisposicoes      = (TextView)findViewById(R.id.trabalhe_conosco_outras_disposicoes);

        chk_Aceito                = (CheckBox) findViewById(R.id.trabalhe_conosco_chk_aceito);
        btnAceito                 = (Button) findViewById(R.id.activity_trabalhe_conosco_btnAceitar);

        mUsuario = (Usuario) getIntent().getSerializableExtra(Constants.USUARIO_EXTRA);

        carregaTermoeCondicoes();

    }

    private void carregaTermoeCondicoes() {

        txtRelacContratual.setText("TERMOS E CONDIÇÕES:\n" +
                "Última atualização: 20 de dezembro de 2016\n\n" +
                "1. RELACIONAMENTO CONTRATUAL\n" +
                "Estes Termos de uso (“Termos”) regem seu acesso e uso, como pessoa física, dentro do Brasil, de aplicativos, sites de Internet, conteúdos, produtos e também serviços (os “Serviços”) disponibilizados pela MotoOn Tecnologia., sociedade individual, estabelecida no Brasil, com sede na Rua Silveira Lobo, nº 32, CEP 52.061-030, Recife/PE, inscrita no Cadastro Nacional de Pessoas Jurídicas do Ministério da Fazenda (CNPJ/MF) sob n. 20.278.977/0001-82 (“MotoOn Tecnologia”) ou qualquer de suas afiliadas.\n" +
                "POR FAVOR, LEIA COM ATENÇÃO ESTES TERMOS ANTES DE ACESSAR OU USAR OS SERVIÇOS.\n" +
                "Ao acessar e usar os Serviços você concorda com os presentes termos e condições, que estabelecem o relacionamento contratual entre você e a MotoOn. Se você não concorda com estes Termos, você não pode acessar nem usar os Serviços. Mediante referido acesso e uso, estes Termos imediatamente encerram, substituem e superam todos os acordos, Termos e acertos anteriores entre você e qualquer Afiliada da MotoOn. A MotoOn poderá imediatamente encerrar estes Termos ou quaisquer Serviços em relação a você ou, de modo geral, deixar de oferecer ou negar acesso aos Serviços ou a qualquer parte deles, a qualquer momento e por qualquer motivo.\n" +
                "Termos adicionais poderão se aplicar ao  Serviço, tais como condições para um evento, atividade ou promoção em particular, e esses Termos adicionais serão divulgados em relação aos respectivos Serviços. Termos adicionais são complementares e considerados parte integrante destes Termos para os efeitos dos respectivos Serviços. Termos adicionais prevalecerão sobre estes Termos em caso de conflito com relação aos referidos Serviços.\n" +
                "A MotoOn poderá alterar os Termos relativos aos Serviços a qualquer momento. Aditamentos entrarão em vigor quando a MotoOn fizer a postagem da versão atualizada dos Termos neste local ou das condições atualizadas ou Termos adicionais sobre o respectivo Serviço. O fato de você continuar a acessar ou usar os Serviços após essa postagem representa seu consentimento em vincular-se aos Termos alterados.\n" +
                "Nossa obtenção e uso de informações pessoais associadas aos Serviços está disciplinada na Declaração de Privacidade de Usuários da MotoOn localizada em https://www.motoonbr.com.br/declaracao-de-privacidade/. A MotoOn ou qualquer de suas afiliadas poderá fornecer a uma gerenciadora de reclamações ou seguradoras qualquer informação necessária (inclusive suas informações de contato) se houver qualquer reclamação, litígio ou conflito, o que pode incluir acidente envolvendo você ou Prestadores Terceiros, e essas informações ou dados forem necessários para solucionar a reclamação, litígio ou conflito.\n");

        txtOsServicos.setText("2. OS SERVIÇOS\n" +
                "Os Serviços integram uma plataforma de tecnologia que permite aos(às) Usuários(as) de aplicativos móveis ou sites de Internet da MotoOn, fornecidos como parte dos Serviços (cada qual um “Aplicativo”), providenciar e programar Serviços de transporte com terceiros provedores independentes desses Serviços, inclusive terceiros fornecedores independentes de transporte e mediante contrato com a MotoOn ou determinadas Afiliadas da MotoOn (“Prestadores Terceiros”). A menos que diversamente acordado pela MotoOn em contrato escrito em separado firmado com você, os Serviços são disponibilizados para seu uso pessoal e não comercial. VOCÊ RECONHECE QUE A MOTOON NÃO PRESTA SERVIÇOS DE TRANSPORTE, NEM FUNCIONA COMO TRANSPORTADORA, E QUE TODOS ESSES SERVIÇOS DE TRANSPORTE SÃO PRESTADOS POR PRESTADORES TERCEIROS INDEPENDENTES QUE NÃO SÃO EMPREGADOS(AS) E NEM REPRESENTANTES DA MOTOON, NEM DE QUALQUER DE SUAS AFILIADAS.\n" +
                "LICENÇA.\n" +
                "Sujeito ao cumprimento destes Termos, a MotoOn outorga a você uma licença limitada, não exclusiva, não passível de sublicença, revogável e não transferível para: (i) acesso e uso dos Aplicativos em seu dispositivo pessoal, exclusivamente para o seu uso dos Serviços; e (ii) acesso e uso de qualquer conteúdo, informação e material correlato que possa ser disponibilizado por meio dos Serviços, em cada caso, para seu uso pessoal, nunca comercial. Quaisquer direitos não expressamente outorgados por estes Termos são reservados à MotoOn e suas afiliadas licenciadoras.\n" +
                "RESTRIÇÕES.\n" +
                "Você não poderá: (i) remover qualquer aviso de direito autoral, direito de marca ou outro aviso de direito de propriedade de qualquer parte do Serviço; (ii) reproduzir, modificar, preparar obras derivadas, distribuir, licenciar, locar, vender, revender, transferir, exibir, veicular, transmitir ou, de qualquer outro modo, explorar os Serviços, exceto da forma expressamente permitida pela MotoOn; (iii) decompilar, realizar engenharia reversa ou desmontar os Serviços, exceto conforme permitido pela legislação aplicável; (iv) conectar, espelhar ou recortar qualquer parte dos Serviços; (v) fazer ou lançar quaisquer programas ou scripts com a finalidade de fazer scraping, indexação, pesquisa ou qualquer outra forma de obtenção de dados de qualquer parte dos Serviços, ou de sobrecarregar ou prejudicar indevidamente a operação e/ou funcionalidade de qualquer aspecto dos Serviços; ou (vi) tentar obter acesso não autorizado aos Serviços ou prejudicar qualquer aspecto dos Serviços ou seus sistemas ou redes correlatas.\n" +
                "PRESTAÇÃO DOS SERVIÇOS.\n" +
                "Você reconhece que o Serviço disponibilizado pela MotoOn refere-se a solicitação de  serviços de transporte prestados por terceiros independentes, inclusive a marca de solicitação de transporte atual é referida como “MotoOn”. Você reconhece também que o Serviço esta disponível sob essa marca e opção de solicitação oferecida por: (i) Prestadores Terceiros independentes, inclusive motociclistas de empresas de rede de transporte, detentores(as) de permissão para Serviços de transporte na categoria profissional ou detentores(as) de permissões, autorizações ou licenças de transporte similares.\n" +
                "SERVIÇOS E CONTEÚDO DE TERCEIROS(AS).\n" +
                "Os Serviços poderão ser disponibilizados e acessados em conexão com Serviços e conteúdo de terceiros(as) (inclusive publicidade) que a MotoOn não controlará. VOCÊ RECONHECE QUE TERMOS DE USO E POLÍTICAS DE PRIVACIDADE DIFERENTES PODERÃO SER APLICÁVEIS AO USO DESSES SERVIÇOS E CONTEÚDO DE TERCEIROS(AS). A MOTOON NÃO ENDOSSA ESSES SERVIÇOS E CONTEÚDO DE TERCEIROS(AS) E A MOTOON NÃO SERÁ, EM HIPÓTESE ALGUMA, RESPONSÁVEL POR NENHUM PRODUTO OU SERVIÇO DESSES(AS) TERCEIROS(AS) FORNECEDORES(AS). Além disto, Google, Inc., e suas subsidiárias e afiliadas internacionais serão terceiros(as) beneficiários(as) deste Contrato, caso você acesse os Serviços usando aplicativos desenvolvidos para dispositivos móveis baseado em Android, respectivamente. Esses(as) terceiros(as) beneficiários(as) não são partes deste Contrato e não são responsáveis pela prestação dos Serviços ou por qualquer forma de suporte aos Serviços. Seu acesso aos Serviços usando esses dispositivos está sujeito às condições estabelecidas nos termos de serviços dos respectivos terceiros(as) beneficiários(as).\n" +
                "TITULARIDADE.\n" +
                "Os Serviços e todos os direitos sobre eles são e permanecerão de propriedade da MotoOn ou de propriedade das Afiliadas da MotoOn, ou de suas respectivas licenciadoras, conforme o caso. Estes Termos e o uso dos Serviços não lhe outorgam nem lhe conferem qualquer direito: (i) sobre os Serviços, exceto pela licença limitada concedida acima; ou (ii) de usar ou, de qualquer modo, fazer referência a nomes societários, logotipos, nomes de produtos ou de Serviços, marcas comerciais ou marcas de serviço da MotoOn ou de qualquer licenciadora da MotoOn.\n");


        txtUsoDoServico.setText("3. O USO DOS SERVIÇOS\n" +
                "USUÁRIO OU TERCEIRO INDEPENDENTE.\n" +
                "Para utilizar o Serviço, você deve registrar-se e manter uma conta pessoal de usuário de Serviço (“Conta”). Se você for usuário, deve ter pelo menos 10 anos e se você for um terceiro independente deve ter pelo menos 18 anos ou a maioridade exigida por lei em seu foro (se for diferente de 18 anos) para abrir uma Conta. Registro de Conta exige que você apresente à MotoOn certas informações pessoais, tais como seu nome, endereço, número de telefone celular e idade. Você concorda em manter informações corretas, completas e atualizadas em sua Conta. Se você não mantiver informações corretas, completas e atualizadas em sua Conta, você poderá ficar impossibilitado(a) de acessar e usar os Serviços ou a MotoOn poderá resolver estes Termos. Você é responsável por todas as atividades realizadas na sua Conta e concorda em manter sempre a segurança e o sigilo do nome de usuário e senha da sua Conta. A menos que diversamente permitido pela MotoOn por escrito, você poderá manter apenas uma Conta.\n" +
                "CONDUTA E OBRIGAÇÕES DO USUÁRIO.\n" +
                "O Serviço não está disponível para uso para indivíduos menores de 10 anos. Você não poderá autorizar terceiros(as) a usar sua Conta, você não poderá permitir que pessoas menores de 10 anos recebam Serviços de transporte de Prestadores Terceiros, salvo se estiverem em sua companhia. Você não poderá ceder, nem de qualquer outro modo transferir, sua Conta a nenhuma outra pessoa ou entidade. Você concorda em cumprir todas as leis aplicáveis quando usar os Serviços e que somente poderá usar os Serviços para finalidades legítimas (por ex. não transportar materiais ilegais ou perigosos). Você não poderá, quando usar os Serviços, causar transtorno, aborrecimento, inconveniente ou danos à propriedade dos Prestadores Terceiros ou de qualquer outro terceiro. Em determinadas situações, você poderá ser solicitado(a) a fornecer comprovante de identidade para acessar ou usar os Serviços, e concorda que poderá ter seu acesso ou uso dos Serviços negado caso você se recuse a fornecer comprovante de identidade.\n" +
                "CONDUTA E OBRIGAÇÕES DO TERCEIRO INDEPENDENTE.\n" +
                "O Serviço não está disponível para uso para indivíduos menores de 18 anos. Você não poderá autorizar terceiros(as) a usar sua Conta. Você não poderá ceder, nem de qualquer outro modo transferir, sua Conta a nenhuma outra pessoa ou entidade. Você concorda em cumprir todas as leis aplicáveis quando usar os Serviços e que somente poderá usar os Serviços para finalidades legítimas (por ex. transportar passageiros). Você não poderá, quando usar os Serviços, causar transtorno, aborrecimento, inconveniente ou danos ao usuário do serviço. Em determinadas situações, você poderá ser solicitado(a) a fornecer comprovante de identidade para acessar ou usar os Serviços, e concorda que poderá ter seu acesso ou uso dos Serviços negado caso você se recuse a fornecer comprovante de identidade.\n" +
                "É imprescindível que ao atender um chamado, seja feita de modo ágil para não gerar espera por parte do usuário solicitante, o qual esse está habilitado a cancelar a qualquer momento sua solicitação por motivos de sua escolha. Nesse caso o crédito debitado em sua conta será estornado automaticamente, gerando total confiabilidade do serviço.\n" +
                "É permitido a você, prestador terceiro,  após atender o chamado, por algum motivo não puder ir buscar o passageiro, fazer o devido cancelamento, isso implica em disponibilizar a corrida para outro, pois não é feito o cancelamento propriamente dito, e sim volta o status anterior (‘aguardando atendimento’). Nesse cenário, o crédito debitado não é estornado para sua conta, então cuidado ao cancelar uma corrida que você escolheu para atender.\n" +
                "Só será permitido atender um chamado por vez, finalize o chamado após transportar seu cliente no destino. Essa finalização é dada por você ou pelo usuário quando informar que a moto chegou no local de origem.\n" +
                "MENSAGEM DE TEXTO.\n" +
                "Ao criar uma Conta, você concorda que os Serviços poderão lhe enviar mensagens de textos informativas (SMS) como parte das operações comerciais regulares para o uso dos Serviços. Você poderá optar por não receber mensagens de texto (SMS) da MotoOn a qualquer momento enviando e-mail para sac@motoonbr.com.br e indicando que não mais deseja receber essas mensagens, juntamente com o número do telefone celular que as recebe. Você reconhece que ao optar por não receber as mensagens de texto poderá impactar o uso dos Serviços.\n" +
                "CONTEÚDO FORNECIDO PELO(A) USUÁRIO(A).\n" +
                "A MotoOn poderá, a seu exclusivo critério, permitir que você ou qualquer pessoa apresente, carregue, publique ou, de qualquer modo, disponibilize para a MotoOn por meio dos Serviços, conteúdo e informações de texto, áudio ou vídeo, inclusive comentários e feedbacks relacionados aos Serviços, iniciação de solicitação de suporte e registro em concursos e promoções (“Conteúdo de Usuário(a)\"). Qualquer Conteúdo de Usuário(a) fornecido por você permanece de sua propriedade. Contudo, ao fornecer Conteúdo de Usuário(a) para a MotoOn, você outorga a MotoOn e suas afiliadas uma licença em nível mundial, perpétua, irrevogável, transferível, isenta de royalties, e com direito a sublicenciar, usar, copiar, modificar, criar obras derivadas, distribuir, publicar, exibir, executar em público e, de qualquer outro modo, explorar esse Conteúdo de Usuário(a) em todos os formatos e canais de distribuição hoje conhecidos ou desenvolvidos no futuro (inclusive em conexão com os Serviços e com os negócios da MotoOn e em sites e Serviços de terceiros), sem ulterior aviso a você ou seu consentimento, e sem necessidade de pagamento a você ou a qualquer outra pessoa ou entidade.\n" +
                "Você declara e garante que: (i) é o(a) único(a) e exclusivo(a) proprietário(a) de todo Conteúdo de Usuário(a) ou tem todos os direitos, licenças, consentimentos e liberações necessários para outorgar à MotoOn a licença sobre o Conteúdo de Usuário(a) acima referido; e (ii) nem o Conteúdo de Usuário(a) nem sua apresentação, carregamento, publicação ou outra forma de disponibilização desse Conteúdo de Usuário(a) tampouco o uso do Conteúdo de Usuário(a) pela MotoOn da forma aqui permitida infringirá, constituirá apropriação indevida nem violará propriedade intelectual ou direito de propriedade de terceiros(a), nem direitos de publicidade ou privacidade e também não resultarão na violação de qualquer lei ou regulamento aplicável.\n" +
                "Você concorda em não fornecer Conteúdo de Usuário(a) que seja difamatório, calunioso, injurioso, violento, obsceno, pornográfico, ilegal ou de qualquer modo ofensivo, conforme apuração da MotoOn a seu critério exclusivo, seja ou não esse material protegido por lei. A MotoOn poderá, mas não está obrigada a, analisar, monitorar ou remover Conteúdo de Usuário(a), a critério exclusivo da MotoOn, a qualquer momento e por qualquer motivo, sem nenhum aviso a você.\n" +
                "ACESSO À REDE E EQUIPAMENTOS.\n" +
                "Você é responsável por obter o acesso a rede de dados necessário para usar os Serviços. As taxas e encargos de sua rede de dados e mensagens poderão se aplicar se você acessar ou usar os Serviços de um dispositivo sem fio e você será responsável por essas taxas e encargos. Você é responsável por adquirir e atualizar os equipamentos e dispositivos necessários para acessar e usar os Serviços e Aplicativos e quaisquer de suas atualizações. A MOTOON NÃO GARANTE QUE OS SERVIÇOS, OU QUALQUER PARTE DELES, FUNCIONARÃO EM QUALQUER EQUIPAMENTO OU DISPOSITIVO EM PARTICULAR. Além disso, os Serviços poderão estar sujeitos a mau funcionamento e atrasos inerentes ao uso da Internet e de comunicações eletrônicas.\n");



        txtPagamento.setText("4. PAGAMENTO\n" +
                "USUÁRIO\n" +
                "Você entende que os serviços ou bens que você receber de um Prestador Terceiro, contratados por meio dos Serviços, serão cobrados (“Preço”) pelo próprio Prestador  Terceiro. Após você ter recebido serviços por meio do uso do Serviço, a MotoOn não se encarrega pelo recebimento do pagamento do respectivo Preço em nome do Prestador Terceiro na qualidade de agente limitado de cobrança desse Prestador Terceiro. O pagamento deve ser realizado por você diretamente ao Prestador Terceiro. O preço pago por você é final e não reembolsável. A MotoOn não tem quaisquer controle do preço cobrado do serviço pelo Prestador Terceiro. O preço é devido e deve ser pago imediatamente após a prestação do serviço.\n" +
                "Na relação entre você e a MotoOn, a MotoOn não reserva-se o direito de estabelecer, remover e/ou revisar o Preço relativo aos serviços obtidos por meio do uso dos Serviços. Ademais, você reconhece e concorda que o Preço aplicável em certas áreas geográficas poderão (i) incluir tarifas, taxas, impostos e/ou contribuições governamentais devidas em seu nome, inclusive, tarifas de pedágios, conforme a rota tomada pelo Prestador Terceiro e legislação aplicável, e, (ii) aumentar substancialmente quando a oferta de serviços por parte dos Prestadores Terceiros for menor do que a demanda por referidos serviços. durante horários de pico. O pagamento de taxas, impostos e/ou contribuições governamentais, serão de sua responsabilidade e você reembolsará o Prestador Terceiro e/ou a MotoOn por todas tarifas, taxas, impostos e/ou contribuições governamentais pagas em seu nome. A MotoOn envidará esforços razoáveis para informá-lo dos Preços que poderão se aplicar, sendo certo que você será responsável pelo pagamento dos Preços lançados em sua Conta independentemente de estar ciente desses Preços ou de seus valores. Você poderá optar por cancelar sua solicitação de serviços ou bens de um Prestador Terceiro a qualquer momento antes da chegada desse Prestador Terceiro, caso em que poderá incidir uma avaliação negativa.\n" +
                "TERCEIRO INDEPENDENTE \n" +
                "Você entende que para utilização do serviço por você é cobrada (“Preço”) uma taxa de acordo com o pacote de corridas de sua escolha que devem ser paga diretamente a um ponto de recarga, sendo apenas em espécie. A MotoOn facilitará a compra desses pacotes pelo aplicativo através de uma opção de compra o qual passará por uma validação, nesse caso é necessário preencher corretamente as informações de cartão de crédito na sua conta de usuário. O preço incluirá todos os impostos exigidos por lei. O preço pago por você é final e não reembolsável. \n" +
                "Quando pago em dinheiro, os créditos são processados e creditados imediatamente, permitindo o atendimento de chamados da sua região imediatamente. \n" +
                "Na compra com cartão de crédito, seja pelo ponto de recarga ou pelo aplicativo, haverá um prazo de validação para que seja  processado e creditado em sua conta.\n" +
                "Ao se cadastrar como Parceiro Independente você ganha automaticamente o plano de 10cc(cilindradas) que possibilita você atender 10 corridas sem ter que pagar por isso.\n" +
                "Na relação entre você e a MotoOn, a MotoOn reserva-se o direito de estabelecer, remover e/ou revisar o Preço relativo a compras dos pacotes de corridas(Créditos) a qualquer momento, a critério exclusivo da MotoOn.\n\n" +
                "TABELA DE PLANOS\n" +
                "Plano 10 cc (cilindradas) | 10 Corridas | R$ 5,00\n" +
                "Plano 20 cc (cilindradas) | 20 Corridas | R$ 10,00\n" +
                "Plano 30 cc (Cilindradas) | 30 Corridas | R$ 15,00\n" +
                "Plano 45 cc (Cilindradas) | 45 Corridas | R$ 20,00\n" +
                "Plano 70 cc (Cilindradas) | 70 Corridas | R$ 30,00\n" +
                "\n\n" +
                "TAXA DE REPAROS OU LIMPEZA (TERCEIRO INDEPENDENTE).\n" +
                "Você será responsável pelos custos de reparos a danos ou pela limpeza de sua moto resultantes do uso dos Serviços em seu benefício que excedam os danos naturais decorrentes do uso (“Reparos ou Limpeza”).\n");


        txtRecusaGarantia.setText("5. RECUSA DE GARANTIA; LIMITAÇÃO DE RESPONSABILIDADE; INDENIZAÇÃO.\n" +
                "RECUSA DE GARANTIA.\n" +
                "OS SERVIÇOS SÃO PRESTADOS “NO ESTADO” E “COMO DISPONÍVEIS”. A MOTOON RECUSA TODAS AS DECLARAÇÕES E GARANTIAS, EXPRESSAS, IMPLÍCITAS OU LEGAIS, NÃO EXPRESSAMENTE CONTIDAS NESTES TERMOS, INCLUSIVE AS GARANTIAS IMPLÍCITAS DE COMERCIALIZAÇÃO, ADEQUAÇÃO A UMA FINALIDADE ESPECÍFICA E NÃO INFRINGÊNCIA. ADEMAIS, A MOTOON NÃO FAZ NENHUMA DECLARAÇÃO NEM DÁ GARANTIA SOBRE A CONFIABILIDADE, PONTUALIDADE, QUALIDADE, ADEQUAÇÃO OU DISPONIBILIDADE DOS SERVIÇOS OU DE QUAISQUER SERVIÇOS OU BENS SOLICITADOS POR MEIO DO USO DOS SERVIÇOS, NEM QUE OS SERVIÇOS SERÃO ININTERRUPTOS OU LIVRES DE ERROS. A MOTOON NÃO GARANTE A QUALIDADE, ADEQUAÇÃO, SEGURANÇA OU HABILIDADE DE PRESTADORES TERCEIROS. VOCÊ CONCORDA QUE TODO O RISCO DECORRENTE DO USO DOS SERVIÇOS E DE QUALQUER SERVIÇO OU BEM SOLICITADO POR MEIO DA TECNOLOGIA SERÁ SEMPRE SEU NA MÁXIMA MEDIDA PERMITIDA PELA LEI APLICÁVEL.\n" +
                "LIMITAÇÃO DE RESPONSABILIDADE.\n" +
                "A MOTOON NÃO SERÁ RESPONSÁVEL POR DANOS INDIRETOS, INCIDENTAIS, ESPECIAIS, PUNITIVOS OU EMERGENTES, INCLUSIVE LUCROS CESSANTES, PERDA DE DADOS, DANOS MORAIS OU PATRIMONIAIS RELACIONADOS, ASSOCIADOS OU DECORRENTES DO USO DO SERVIÇO AINDA QUE A MOTOON TENHA SIDO ALERTADA PARA A POSSIBILIDADE DESSES DANOS. A MOTOON NÃO SERÁ RESPONSÁVEL POR NENHUM DANO, OBRIGAÇÃO OU PREJUÍZO DECORRENTE DO: (i) SEU USO DOS SERVIÇOS OU SUA INCAPACIDADE DE ACESSAR OU USAR OS SERVIÇOS; OU (ii) QUALQUER OPERAÇÃO OU RELACIONAMENTO ENTRE VOCÊ E QUALQUER PRESTADOR TERCEIRO, AINDA QUE A MOTOON TENHA SIDO ALERTADA PARA A POSSIBILIDADE DESSES DANOS. A MOTOON NÃO SERÁ RESPONSÁVEL POR ATRASOS OU FALHAS DECORRENTES DE CAUSAS FORA DO CONTROLE RAZOÁVEL DA MOTOON. VOCÊ RECONHECE QUE PRESTADORES TERCEIROS QUE PRESTAREM SERVIÇOS DE TRANSPORTE SOLICITADOS POR MEIO DA MARCA OFERECE SERVIÇO DE TRANSPORTE DO TIPO PONTO A PONTO.\n" +
                "VOCÊ CONCORDA QUE QUALQUER PAGAMENTO FEITO A VOCÊ COM BASE NO SEGURO CONTRA ACIDENTES PESSOAIS DE PASSAGEIROS (APP) MANTIDO PELA MOTOON OU PELO PRESTADOR TERCEIRO REDUZIRÁ QUALQUER INDENIZAÇÃO DEVIDA A VOCÊ DECORRENTE DAQUELE MESMO ACIDENTE.\n" +
                "OS SERVIÇOS DA MOTOON PODERÃO SER USADOS POR VOCÊ PARA SOLICITAR E PROGRAMAR SERVIÇOS DE TRANSPORTE PRESTADOS POR PRESTADORES TERCEIROS, MAS VOCÊ CONCORDA QUE A MOTOON NÃO TEM RESPONSABILIDADE EM RELAÇÃO A VOCÊ, POR CONTA DE QUALQUER SERVIÇO DE TRANSPORTE REALIZADOS POR PRESTADORES TERCEIROS, SALVO SE EXPRESSAMENTE ESTABELECIDA NESTES TERMOS. COMO CONSEQUÊNCIA, A MOTOON NÃO TEM QUALQUER RESPONSABILIDADE POR ROTAS ADOTADAS POR PRESTADORES TERCEIROS OU POR QUAISQUER ITENS PERDIDOS DURANTE SUA TRAJETÓRIA.\n" +
                "AS LIMITAÇÕES E RECUSA DE GARANTIAS CONTIDAS NESTA CLÁUSULA 5 NÃO POSSUEM O OBJETIVO DE LIMITAR RESPONSABILIDADES OU ALTERAR DIREITOS DE CONSUMIDOR QUE DE ACORDO COM A LEI APLICÁVEL NÃO PODEM SER LIMITADOS OU ALTERADOS.\n" +
                "INDENIZAÇÃO.\n" +
                "Você concorda em indenizar e manter a MotoOn, seus diretores(as), conselheiros(as), empregados(as) e agentes isentos(as) de responsabilidade por todas e quaisquer reclamações, cobranças, prejuízos, responsabilidades e despesas (inclusive honorários advocatícios) decorrentes ou relacionados: (i) ao uso dos Serviços, de serviços ou bens obtidos por meio do uso dos Serviços; (ii) descumprimento ou violação de qualquer disposição destes Termos; (iii) o uso, pela MotoOn, do Conteúdo de Usuário(a); ou (iv) violação dos direitos de terceiros, inclusive Prestadores Terceiros.\n");


        txtLegislacaoAplicavel.setText("6. LEGISLAÇÃO APLICÁVEL; JURISDIÇÃO.\n" +
                "Estes Termos serão regidos e interpretados exclusivamente de acordo com as leis do Brasil. Qualquer reclamação, conflito ou controvérsia que surgir deste contrato ou a ele relacionada, inclusive que diga respeito a sua validade, interpretação ou exequibilidade, será solucionada exclusivamente pelos tribunais do foro de seu domicílio.\n");

        txtOutrasDisposicoes.setText("7. OUTRAS DISPOSIÇÕES\n" +
                "AVISOS.\n" +
                "A MotoOn poderá enviar avisos por meio de notificações gerais nos Serviços, correio eletrônico para seu endereço de e-mail em sua Conta, ou por comunicação escrita enviada ao endereço indicado em sua Conta. Você poderá notificar a MotoOn por meio de comunicação por meio eletrônico para sac@motoonbr.com.br ou fazer comunicação escrita para o endereço da MotoOn na Rua Silveira Lobo, nº 32, CEP 52.061-030, Recife/PE, Brasil.\n" +
                "DISPOSIÇÕES GERAIS.\n" +
                "Você não poderá ceder tampouco transferir estes Termos, total ou parcialmente, sem prévia aprovação por escrito da MotoOn. Você concede sua aprovação para que a MotoOn ceda e transfira estes Termos total ou parcialmente, inclusive: (i) para uma subsidiária ou afiliada; (ii) um adquirente das participações acionárias, negócios ou bens da MotoOn; ou (iii) para um sucessor em razão de qualquer operação societária. Não existe joint-venture, sociedade, emprego ou relação de representação entre você, a MotoOn ou quaisquer Prestadores Terceiros como resultado do contrato entre você e a MotoOn ou pelo uso dos Serviços.\n" +
                "Caso qualquer disposição destes Termos seja tida como ilegal, inválida ou inexequível total ou parcialmente, por qualquer legislação, essa disposição ou parte dela será, naquela medida, considerada como não existente para os efeitos destes Termos, mas a legalidade, validade e exequibilidade das demais disposições contidas nestes Termos não serão afetadas. Nesse caso, as partes substituirão a disposição ilegal, inválida ou inexequível, ou parte dela, por outra que seja legal, válida e exequível e que, na máxima medida possível, tenha efeito similar à disposição tida como ilegal, inválida ou inexequível para fins de conteúdo e finalidade dos presentes Termos. Estes Termos constituem a totalidade do acordo e entendimento das partes sobre este assunto e substituem e prevalecem sobre todos os entendimentos e compromissos anteriores sobre este assunto. Nestes Termos, as palavras “inclusive” e “inclui” significam “incluindo, sem limitação”.\n");


    }

    public void btnAceitarOnClick(View view) {
        Intent it = new Intent(this, AceitouTermoActivity.class);
        it.putExtra(Constants.USUARIO_EXTRA, mUsuario);
        startActivity(it);
    }

    public void chk_aceitoOnClick(View view) {

        if(chk_Aceito.isChecked()){
            btnAceito.setEnabled(true);
        }
        else
        {
            btnAceito.setEnabled(false);
        }

    }
}
