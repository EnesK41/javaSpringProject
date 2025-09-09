<markdown:README Dosyası (Formatlanmış):README.md

SecretNews - Full-Stack Haber ve İçerik Platformu
![SecretNews Logo](./images/Logo.png)
SecretNews, Java (Spring Boot) ve React.js kullanılarak geliştirilmiş, topluluk tabanlı, full-stack bir haber ve içerik platformudur. Proje, harici bir REST API'den güncel haberleri çekme ve kullanıcıların "yayıncı" rolüyle kendi kurgusal, SCP-vari "gizli" haberlerini oluşturup paylaşabildiği çift katmanlı bir yapıya sahiptir.

Bu proje, modern yazılım geliştirme pratiklerini, güvenli API tasarımını ve dinamik frontend yönetimini sergilemek amacıyla bir staj projesi olarak geliştirilmiştir.

![SecretNews Anasayfa](./images/SecretNews.png)

💡 Temel Özellikler
Çift Katmanlı Haber Akışı: Hem Brave Search API'den çekilen gerçek dünya haberleri hem de kullanıcılar tarafından oluşturulan "SecretNews" içerikleri.

Rol Bazlı Yetkilendirme (JWT): USER ve PUBLISHER rolleri için Spring Security ve JWT tabanlı, güvenli bir kimlik doğrulama ve yetkilendirme sistemi.

İçerik Yönetimi: Yayıncıların kendi "SecretNews" yazılarını oluşturmasına, silmesine ve yönetmesine olanak tanıyan tam CRUD fonksiyonelliği.

Etkileşim ve Puan Sistemi: Kullanıcıların ve yayıncıların, okunan haberler üzerinden puan kazandığı, etkileşimi teşvik eden bir sistem.

Akıllı API Önbellekleme (Smart Caching): Harici API çağrı limitlerini verimli kullanmak için geliştirilmiş, sunucu taraflı bir önbellekleme mekanizması.

Dinamik Filtreleme: API haber akışını ülkeye ve arama terimine göre filtreleme imkanı.

Responsive Tasarım: Tailwind CSS ve SCSS kullanılarak oluşturulmuş, mobil uyumlu ve modern bir kullanıcı arayüzü.

🛠️ Kullanılan Teknolojiler
Kategori	Teknoloji
Backend	Java, Spring Boot, Spring Security, Spring Data JPA, Hibernate, Maven
Frontend	React.js, JavaScript, HTML5, SCSS, Tailwind CSS, Flowbite React, Axios
Veritabanı	H2 In-Memory Database (geliştirme için)
API	Brave Search API (harici entegrasyon)
Araçlar	Git, GitHub, VS Code

🗺️ Gelecek Planları ve Yol Haritası
Bu projenin temel özellikleri tamamlanmış olup, topluluk etkileşimini artırmak için aşağıdaki özelliklerin eklenmesi planlanmaktadır:

[ ] Kullanıcı Yer İmleri (Bookmarking): Kullanıcıların favori haberlerini kendi profillerine kaydetme özelliği.

[ ] Emoji Reaksiyonları: Kullanıcıların haberlere ve hikayelere emoji ile tepki verebilmesi.

[ ] Yorum Sistemi: Her haberin altında kullanıcıların tartışabileceği bir yorum bölümü.

[ ] Canlıya Alma (Deployment): Projenin Vercel (frontend) ve Render (backend) gibi platformlar kullanılarak bir alan adına (domain) yayınlanması.
