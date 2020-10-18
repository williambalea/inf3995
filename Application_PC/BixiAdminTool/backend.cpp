#include "backend.h"
#include <iostream>

BackEnd::BackEnd(QObject *parent) : QObject(parent)
{
    m_userName = "allo";
}

QString BackEnd::userName()
{
    return m_userName;
}

void BackEnd::setuserName(const QString &userName)
{
    if (userName == m_userName)
        return;
    m_userName = userName;
    emit userNameChanged();
}

QString BackEnd::sqlData()
{
    return m_sqlData;
}

void BackEnd::setSqlData(const QString &data)
{
    m_sqlData = data;
    emit sqlDataChanged();
}

void BackEnd::refresh()
{
    // Set our network manager
    QNetworkAccessManager *man = new QNetworkAccessManager(this);
    connect(man, &QNetworkAccessManager::finished, this, &BackEnd::sqlFinished);

    // configure
    QNetworkRequest req;
    QSslConfiguration sslconf;
    sslconf.setPeerVerifyMode(QSslSocket::VerifyNone); // TODO : changer quand on aura un CA
    req.setSslConfiguration(sslconf);
    req.setUrl(QUrl("https://10.0.0.105/server/survey"));

    // GET
    man->get(req);
}

void BackEnd::sqlFinished(QNetworkReply *reply)
{
    QString data = QString::fromStdString(reply->readAll().toStdString());
    setSqlData(data);
    std::cout << "dev log : " << sqlData().toStdString() << std::endl; //TODO: to remove
}
