#include "backend.h"
#include <iostream>

BackEnd::BackEnd(QObject *parent) : QObject(parent)
{
    // Set our network manager
    man = new QNetworkAccessManager(this);
}


QString BackEnd::sqlData()
{
    return m_sqlData;
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
    connect(man, &QNetworkAccessManager::finished, this, &BackEnd::sqlFinished);
    // TODO: make this request only if admin authentified correctly
    QNetworkRequest req = makeRequest(QUrl("https://" + m_host + "/server/survey"));
    man->get(req);
}

void BackEnd::login() {
    connect(man, &QNetworkAccessManager::finished, this, &BackEnd::loginFinished);

    QNetworkRequest req = makeRequest(QUrl("https://" + m_host + "/server/user/login"));
    QString auth = m_user + ":" + m_pass;
    QByteArray data = auth.toLocal8Bit().toBase64();
    QString headerData = "Basic " + data;
    req.setRawHeader("Authorization", headerData.toLocal8Bit());
    man->get(req);
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

QNetworkRequest BackEnd::makeRequest(const QUrl &url) {
    QNetworkRequest req;
    QSslConfiguration sslconf;
    sslconf.setPeerVerifyMode(QSslSocket::VerifyNone);
    req.setSslConfiguration(sslconf);
    req.setUrl(url);
    return req;
}
