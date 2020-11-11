#include "backend.h"
#include <iostream>
#include <QTimer>

BackEnd::BackEnd(QObject *parent) : QObject(parent)
{
    setupNetworkManagers();
    checkEngines();

    timer = new QTimer(this);
    connect(timer, SIGNAL(timeout()), this, SLOT(periodicFn()));
    timer->start(10000);
}

BackEnd::~BackEnd()
{
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
}


QString BackEnd::sqlData()
{
    qDebug() << m_sqlData.size();
    if (m_sqlData.size() > 4)
        return m_sqlData;
    else
        return "{}";
}

QString BackEnd::user()
{
    return m_user;
}

QString BackEnd::pass()
{
    return m_pass;
}

bool BackEnd::isLoggedIn()
{
    return m_isLoggedIn;
}

void BackEnd::setUser(const QString &data)
{
    m_user = data;
    emit userChanged();
}

void BackEnd::setPass(const QString &data)
{
    m_pass = data;
    emit passChanged();
}

void BackEnd::setSqlData(const QString &data)
{
    m_sqlData = data;
    emit sqlDataChanged();
}

void BackEnd::refresh()
{
    // TODO: make this request only if admin authentified correctly
    QNetworkRequest req = makeRequest(QUrl("https://" + m_host + "/server/survey"));
    manSqlData->get(req);
}

void BackEnd::login() {
    QNetworkRequest req = makeRequest(QUrl("https://" + m_host + "/server/user/login"));
    QString auth = m_user + ":" + m_pass;
    QByteArray data = auth.toLocal8Bit().toBase64();
    QString headerData = "Basic " + data;
    req.setRawHeader("Authorization", headerData.toLocal8Bit());
    manLogin->get(req);
}

void BackEnd::sqlFinished(QNetworkReply *reply)
{
    QString data = QString::fromStdString(reply->readAll().toStdString());
    qDebug() << data << '\n';
    setSqlData(data);
}

void BackEnd::loginFinished(QNetworkReply *reply) {
    QVariant code = reply->attribute(QNetworkRequest::HttpStatusCodeAttribute);
    qDebug() << m_user << ':' << m_pass << '\n';
    qDebug() << code.toString() << '\n';
    if (code == "200") {
        m_isLoggedIn = true;
        emit isLoggedInChanged();
    } else {
        attemps = true;
        emit attempsChanged();
    }
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

void BackEnd::periodicFn() {
    checkEngines();
    //TODO: check les logs
}
