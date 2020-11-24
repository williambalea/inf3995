#include "backend.h"
#include <iostream>
#include <QTimer>
#include <QJsonObject>
#include <QJsonDocument>
#include <QJsonArray>

#define CHECK_ENGINE_INTERVALL 5000
#define HTTP_OK 200
#define HTTP_SERVER_ERR 500
#define NULL_JSON_SIZE 4
#define IS_RUNNING "UP"

#define HTTPS "https://"
#define SERVER_PATH    "/server"
#define STATUS_PATH    "/server/status"
#define CHANGE_PW_PATH "/server/user/password"
#define LOGIN_PATH     "/server/user/login"
#define SURVEY_PATH    "/server/survey"

#define SURVEY_MAN "sqlData"
#define LOGIN_MAN  "login"
#define ENGINE_MAN "enginesStatus"
#define PW_MAN     "changePw"
#define SERVER_MAN "serverConn"

/**
 * PUBLIC functions
 */

BackEnd::BackEnd(QObject *parent) : QObject(parent) {
    setupNetworkManagers();

    timer = new QTimer(this);
    connect(timer, SIGNAL(timeout()), this, SLOT(checkEngines()));
}

BackEnd::~BackEnd() {
    for (const auto& man : mans)
        delete man.second;

    delete timer;
}

QString BackEnd::sqlData() {
    return (m_sqlData.size() > NULL_JSON_SIZE) ? m_sqlData : "{}";
}

/**
 * INVOKABLE function
 */

void BackEnd::refresh() {
    QNetworkRequest req = makeRequest(QUrl(HTTPS + m_host + SURVEY_PATH));
    setAuthHeader(req, m_user, m_pass);
    mans[SURVEY_MAN]->get(req);
}

void BackEnd::login(QString user, QString pass) {
    QNetworkRequest req = makeRequest(QUrl(HTTPS + m_host + LOGIN_PATH));
    setAuthHeader(req, user, pass);
    mans[LOGIN_MAN]->get(req);
}

void BackEnd::changePw(QString old, QString newPass) {
    QNetworkRequest req = makeRequest(QUrl(HTTPS + m_host + CHANGE_PW_PATH));
    setAuthHeader(req, m_user, old);
    QJsonObject obj;
    obj["new"] = newPass;
    mans[PW_MAN]->put(req, QJsonDocument(obj).toJson());
}

void BackEnd::serverConn(QString host) {
    QNetworkRequest req = makeRequest(QUrl(HTTPS + host + SERVER_PATH));
    mans[SERVER_MAN]->get(req);
}

void BackEnd::startTimer() {
    checkEngines();
    timer->start(CHECK_ENGINE_INTERVALL);
}

bool BackEnd::engineStatus(int statusArrayNumber) {
    return enginesStatus[statusArrayNumber];
}

/**
 * PRIVATE SLOTS functions
 */

void BackEnd::sqlFinished(QNetworkReply *reply) {
    m_sqlData = QString::fromStdString(reply->readAll().toStdString());
    emit sqlDataChanged();
}

void BackEnd::loginFinished(QNetworkReply *reply) {
    QVariant code = reply->attribute(QNetworkRequest::HttpStatusCodeAttribute);
    emit loginChanged(code == HTTP_OK);
}

void BackEnd::checkEnginesFinished(QNetworkReply *reply) {
    QVariant code = reply->attribute(QNetworkRequest::HttpStatusCodeAttribute);
    if (code == HTTP_OK || code == HTTP_SERVER_ERR) {
        QByteArray result = reply->readAll();
        QJsonDocument jsonResponse = QJsonDocument::fromJson(result);
        QJsonObject body = jsonResponse.object();
        QStringList status = body["message"].toString().split(QLatin1Char(' '));

        for (int i = 0; i < NB_OF_ENGINES; i++)
            enginesStatus[i] = (status[i] == IS_RUNNING);

    } else {

        for (auto& status : enginesStatus)
            status = false;
    }

    emit enginesStatusChanged();
}

void BackEnd::changePwFinished(QNetworkReply *reply) {
    QVariant code = reply->attribute(QNetworkRequest::HttpStatusCodeAttribute);
    bool isSuccessful = code == HTTP_OK;
    emit passwordChanged(isSuccessful);
}

void BackEnd::serverConnFinished(QNetworkReply *reply) {
    QVariant code = reply->attribute(QNetworkRequest::HttpStatusCodeAttribute);
    emit serverConnChanged(code == HTTP_OK);
}

void BackEnd::setupNetworkManagers() {
    createNetworkManager(SURVEY_MAN, &BackEnd::sqlFinished);
    createNetworkManager(LOGIN_MAN,  &BackEnd::loginFinished);
    createNetworkManager(ENGINE_MAN, &BackEnd::checkEnginesFinished);
    createNetworkManager(PW_MAN,     &BackEnd::changePwFinished);
    createNetworkManager(SERVER_MAN, &BackEnd::serverConnFinished);
}

void BackEnd::createNetworkManager(QString manName, void (BackEnd::* slot) (QNetworkReply *)) {
    mans[manName] = new QNetworkAccessManager(this);
    connect(mans[manName], &QNetworkAccessManager::finished, this, slot);

}

QNetworkRequest BackEnd::makeRequest(const QUrl &url) {
    QNetworkRequest req;
    QSslConfiguration sslconf;
    sslconf.setPeerVerifyMode(QSslSocket::VerifyNone);
    req.setSslConfiguration(sslconf);
    req.setUrl(url);
    return req;
}

void BackEnd::checkEngines() {
    QNetworkRequest req = makeRequest(QUrl(HTTPS + m_host + STATUS_PATH));
    mans[ENGINE_MAN]->get(req);
}

void BackEnd::setAuthHeader(QNetworkRequest &req, QString user, QString pass) {
    QString auth = user + ":" + pass;
    QByteArray data = auth.toLocal8Bit().toBase64();
    QString headerData = "Basic " + data;
    req.setRawHeader("Authorization", headerData.toLocal8Bit());
    req.setHeader(QNetworkRequest::ContentTypeHeader, "application/json");
}
