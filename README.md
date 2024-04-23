# AEDS3
**Algoritmo e Estrutura de Dados 3 - PUC MINAS **
** Resumo da minha solução:**
Na minha solução o tratamento para reocupação dos espaços vazios são feitos em memória principal. Como? Há um arquivo indexado de deletados, que é lido assim que o programa é inicializado, e assim é armazenado as informações em uma árvore, TreeMap (chave: tamanho, valor: posição).
Assim que oocrre qualquer atualização na TreeMap, é "sobrescrito" o arquivo indexado de deletados. Por que? Para que caso haja algum problema enquanto o programa rode, não seja perdido as infomações de quais espaços estão vazios!
Ao encerrar o programa o arquivo de deletados é "sobrescrito" conforme o <key,value> presentres na árvore.

**Respondendo questionário:**
