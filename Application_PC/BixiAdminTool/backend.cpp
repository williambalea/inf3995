#include "backend.h"
#include <iostream>
#include <QTimer>
#include <QJsonObject>
#include <QJsonDocument>

#define CHECK_ENGINE_INTERVALL 1000

BackEnd::BackEnd(QObject *parent) : QObject(parent) {
    setupNetworkManagers();
    checkEngines();

    timer = new QTimer(this);
    connect(timer, SIGNAL(timeout()), this, SLOT(periodicFn()));
    timer->start(CHECK_ENGINE_INTERVALL);


}

BackEnd::~BackEnd() {
    delete manServerConn;
    delete manSqlData;
    delete manLogin;
    delete manEnginesStatus;
    delete timer;
}

void BackEnd::setupNetworkManagers() {
    manSqlData = new QNetworkAccessManager(this);
    connect(manSqlData, &QNetworkAccessManager::finished, this, &BackEnd::sqlFinished);

    manLogin = new QNetworkAccessManager(this);
    connect(manLogin, &QNetworkAccessManager::finished, this, &BackEnd::loginFinished);

    manEnginesStatus = new QNetworkAccessManager(this);
    connect(manEnginesStatus, &QNetworkAccessManager::finished, this, &BackEnd::checkEnginesFinished);

    manChangePw = new QNetworkAccessManager(this);
    connect(manChangePw, &QNetworkAccessManager::finished, this, &BackEnd::changePwFinished);

    manServerConn = new QNetworkAccessManager(this);
    connect(manServerConn, &QNetworkAccessManager::finished, this, &BackEnd::serverConnFinished);
}


QString BackEnd::sqlData() {
    return (m_sqlData.size() > 4) ? m_sqlData : "{}";
}

void BackEnd::setSqlData(const QString &data) {
    m_sqlData = data;
    emit sqlDataChanged();
}

void BackEnd::refresh() {
    QNetworkRequest req = makeRequest(QUrl("https://" + m_host + "/server/survey"));
    setAuthHeader(req, m_user, m_pass);
    manSqlData->get(req);
}

void BackEnd::login(QString user, QString pass) {
    QNetworkRequest req = makeRequest(QUrl("https://" + m_host + "/server/user/login"));
    setAuthHeader(req, user, pass);
    manLogin->get(req);
}


void BackEnd::sqlFinished(QNetworkReply *reply) {
    QString data = QString::fromStdString(reply->readAll().toStdString());
    setSqlData(data);
}

void BackEnd::loginFinished(QNetworkReply *reply) {
    QVariant code = reply->attribute(QNetworkRequest::HttpStatusCodeAttribute);
    emit loginChanged(code == "200");
}

void BackEnd::serverConnFinished(QNetworkReply *reply) {
    QVariant code = reply->attribute(QNetworkRequest::HttpStatusCodeAttribute);
    emit serverConnChanged(code == "200");
}

void BackEnd::checkEnginesFinished(QNetworkReply *reply) {
    QString data = QString::fromStdString(reply->readAll().toStdString());
    QVariant code = reply->attribute(QNetworkRequest::HttpStatusCodeAttribute);
    int httpCode = code.toInt();
    QStringList status = data.split(QLatin1Char(' '));
    if (httpCode == 200) {
        m_engine1Status = (status[0] == "true");
        m_engine2Status = (status[1] == "true");
        m_engine3Status = (status[2] == "true");
    } else {
        m_engine1Status = false;
        m_engine2Status = false;
        m_engine3Status = false;
    }
    emit enginesStatusChanged();
}

void BackEnd::changePwFinished(QNetworkReply *reply) {
    QVariant code = reply->attribute(QNetworkRequest::HttpStatusCodeAttribute);
    bool isSuccessful = code == "200";
    emit passwordChanged(isSuccessful);
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
    QNetworkRequest req = makeRequest(QUrl("https://" + m_host + "/server/status"));
    manEnginesStatus->get(req);
}

void BackEnd::sendFakeLogs() {
    emit log1Changed("super long text to be displayed sdflksn lkjsdf ljk sadf \n");
}

void BackEnd::changePw(QString old, QString newPass) {
    QNetworkRequest req = makeRequest(QUrl("https://" + m_host + "/server/user/password"));
    setAuthHeader(req, m_user, old);
    QJsonObject obj;
    obj["new"] = newPass;
    manChangePw->put(req, QJsonDocument(obj).toJson());
}

void BackEnd::serverConn(QString host) {
    QNetworkRequest req = makeRequest(QUrl("https://" + host + "/server"));
    manServerConn->get(req);
}

void BackEnd::periodicFn() {
    checkEngines();
    sendFakeLogs();
}

void BackEnd::setAuthHeader(QNetworkRequest &req, QString user, QString pass) {
    QString auth = user + ":" + pass;
    QByteArray data = auth.toLocal8Bit().toBase64();
    QString headerData = "Basic " + data;
    req.setRawHeader("Authorization", headerData.toLocal8Bit());
    req.setHeader(QNetworkRequest::ContentTypeHeader, "application/json");
}
