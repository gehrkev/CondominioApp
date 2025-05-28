# Condomínio App

## Setup do Projeto

### Configuração das Chaves API

Para executar o projeto localmente, você precisa configurar as chaves API:

1. **Copie o arquivo template:**
   ```bash
   cp gradle.properties.template gradle.properties
   ```

2. **Obtenha sua chave do Google Maps API:**
    - Acesse o [Google Cloud Console](https://console.cloud.google.com/)
    - Crie um novo projeto ou selecione um existente
    - Ative a API "Maps SDK for Android"
    - Crie uma chave API em "Credenciais"
    - [Tutorial completo aqui](https://developers.google.com/maps/documentation/android-sdk/get-api-key)

3. **Configure sua chave no gradle.properties:**
   ```properties
   GOOGLE_MAPS_API_KEY="sua_chave_api_real_aqui"
   ```

4. **Execute o projeto:**
    - Abra no Android Studio
    - Sync Gradle
    - Execute normalmente

### ⚠️ Importante
- **NUNCA** commite o arquivo `gradle.properties` com chaves reais
- Use sempre o template para novos clones do repositório
- As chaves ficam seguras e não são expostas no código

## Funcionalidades

- Gerenciamento de condomínios
- Cadastro de blocos e apartamentos
- Gestão de locatários
- Consulta automática de CEP
- Visualização no Google Maps

## Arquitetura

- **MVVM** (Model-View-ViewModel)
- **Repository Pattern**
- **SQLite** para persistência local
- **Retrofit** para chamadas de API
- **LiveData** para observação de dados