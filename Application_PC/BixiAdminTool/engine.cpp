#include "engine.h"
#include <QJsonObject>
#include <QJsonDocument>
#include <QJsonArray>

#define CHECK_ENGINE_INTERVALL 5000
#define HTTP_OK 200
#define MAX_TEXTLOG_SIZE 200
#define JSON_BYTES_NB "byte"
#define JSON_LOGS "logs"

Engine::Engine(QObject *parent) : QObject(parent) {
    man = new QNetworkAccessManager(this);
    connect(man, &QNetworkAccessManager::finished, this, &Engine::logsFinished);
    getLogs();

    timer = new QTimer(this);
    connect(timer, SIGNAL(timeout()), this, SLOT(getLogs()));
    timer->start(CHECK_ENGINE_INTERVALL);
}

Engine::~Engine() {
    delete man;
    delete timer;
}

void Engine::getLogs() {
    QString bytesToAsk = QString::number(bytesReceived);
    QString str_id = QString::number(id);
    QUrl url = QUrl("https://" + host + "/engine" + str_id + "/logs/" + bytesToAsk);
    QNetworkRequest req = makeRequest(url);
    setAuthHeader(req, user, pass);
    man->get(req);
}

void Engine::logsFinished(QNetworkReply *reply) {
    int code = reply->attribute(QNetworkRequest::HttpStatusCodeAttribute).toInt();
    QByteArray body = reply->readAll();
    logsReplyHandler(code, body);
    reply->deleteLater();
}

void Engine::logsReplyHandler(int code, QByteArray replyRead) {
    if (code != HTTP_OK) return;
    QJsonDocument jsonResponse = QJsonDocument::fromJson(replyRead);
    QJsonArray body = jsonResponse.array();
    bytesReceived = body.at(0).toObject().value(JSON_BYTES_NB).toInt();
    int max = body.size();
    for (int i = 1; i < max; i++) {
        QString data = body.at(i).toObject().value(JSON_LOGS).toString();
        bool isText = data.size() < MAX_TEXTLOG_SIZE;
        emit logChanged(data, isText);
    }
}

QNetworkRequest Engine::makeRequest(const QUrl &url) {
    QNetworkRequest req;
    QSslConfiguration sslconf;
    sslconf.setPeerVerifyMode(QSslSocket::VerifyNone);
    req.setSslConfiguration(sslconf);
    req.setUrl(url);
    return req;
}

void Engine::setAuthHeader(QNetworkRequest &req, QString user, QString pass) {
    QString auth = user + ":" + pass;
    QByteArray data = auth.toLocal8Bit().toBase64();
    QString headerData = "Basic " + data;
    req.setRawHeader("Authorization", headerData.toLocal8Bit());
    req.setHeader(QNetworkRequest::ContentTypeHeader, "application/json");
}
