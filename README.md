<markdown:README Dosyası (Formatlanmış):README.md

SecretNews - Full-Stack Haber ve İçerik Platformu
![SecretNews Logo](./images/Logo.png)
SecretNews, Java (Spring Boot) ve React.js kullanılarak geliştirilmiş, topluluk tabanlı, full-stack bir haber ve içerik platformudur. Proje, harici bir REST API'den güncel haberleri çekme ve kullanıcıların "yayıncı" rolüyle kendi kurgusal, SCP-vari "gizli" haberlerini oluşturup paylaşabildiği çift katmanlı bir yapıya sahiptir.

Bu proje, modern yazılım geliştirme pratiklerini, güvenli API tasarımını ve dinamik frontend yönetimini sergilemek amacıyla bir staj projesi olarak geliştirilmiştir.

[SecretNews Anasayfa](./images/SecretNews.png)

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

Kesinlikle harika bir fikir. Projenin GitHub'daki README dosyası, projenin vitrinidir ve onu profesyonel bir şekilde hazırlamak çok önemlidir. Sorduğun tüm sorular çok yerinde ve projenin değerini artıracak adımlar.

Evet, resim (hatta GIF) ekleyebilirsin ve planladığın özellikleri bir "Yol Haritası" olarak belirtmek, projenin ne kadar canlı ve gelecek vaat eden bir proje olduğunu gösterir.

Senin için, projemizin tüm detaylarını içeren, bir işe alım uzmanını veya başka bir geliştiriciyi etkileyecek, profesyonel bir README şablonu hazırladım. Bu şablonu doğrudan projenin README.md dosyasına yapıştırabilirsin.

README Şablonu
Aşağıdaki metni projenin ana dizinindeki README.md dosyasına yapıştırabilirsin. [] içindeki yerleri kendi ekran görüntülerinle ve bilgilerinle doldurman yeterli olacak.

<markdown:README Dosyası:README.md

SecretNews - Full-Stack Haber ve İçerik Platformu
SecretNews, Java (Spring Boot) ve React.js kullanılarak geliştirilmiş, topluluk tabanlı, full-stack bir haber ve içerik platformudur. Proje, harici bir REST API'den güncel haberleri çekme ve kullanıcıların "yayıncı" rolüyle kendi kurgusal, SCP-vari "gizli" haberlerini oluşturup paylaşabildiği çift katmanlı bir yapıya sahiptir.

Bu proje, modern yazılım geliştirme pratiklerini, güvenli API tasarımını ve dinamik frontend yönetimini sergilemek amacıyla bir staj projesi olarak geliştirilmiştir.

Projeden Ekran Görüntüsü
İpucu: Uygulamanın en güzel görünen sayfasının (örneğin ana sayfa haber akışının) bir ekran görüntüsünü alıp, projendeki bir images klasörüne ekleyip buraya link verebilirsin.

![SecretNews Ana Sayfa Görünümü](./images/screenshot.png)

💡 Temel Özellikler
Çift Katmanlı Haber Akışı: Hem Brave Search API'den çekilen gerçek dünya haberleri hem de kullanıcılar tarafından oluşturulan "SecretNews" içerikleri.

Rol Bazlı Yetkilendirme (JWT): USER ve PUBLISher rolleri için Spring Security ve JWT tabanlı, güvenli bir kimlik doğrulama ve yetkilendirme sistemi.

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
Araçlar	Git, GitHub, Postman, VS Code

E-Tablolar'a aktar
🚀 Projeyi Yerelde Çalıştırma
Projeyi kendi bilgisayarınızda çalıştırmak için aşağıdaki adımları izleyebilirsiniz.

Ön Gereksinimler
Java JDK 17 veya üstü

Apache Maven

Node.js ve npm

Backend Kurulumu
Projeyi klonlayın: git clone https://github.com/EnesK41/javaSpringProject.git

Backend klasörüne gidin: cd javaSpringProject/backend

src/main/resources/ dizininde application.properties.example dosyasının bir kopyasını oluşturun ve adını application.properties olarak değiştirin.

application.properties dosyasını açın ve Brave Search API'den aldığınız kendi API anahtarınızı ilgili alana girin.

Uygulamayı çalıştırın: mvn spring-boot:run

Backend sunucusu http://localhost:8080 adresinde çalışmaya başlayacaktır.

Frontend Kurulumu
Yeni bir terminal açın ve frontend klasörüne gidin: cd javaSpringProject/frontend

Gerekli paketleri yükleyin: npm install

Uygulamayı başlatın: npm start

Frontend geliştirme sunucusu http://localhost:3000 adresinde açılacaktır.

🗺️ Gelecek Planları ve Yol Haritası
Bu projenin temel özellikleri tamamlanmış olup, topluluk etkileşimini artırmak için aşağıdaki özelliklerin eklenmesi planlanmaktadır:

[ ] Kullanıcı Yer İmleri (Bookmarking): Kullanıcıların favori haberlerini kendi profillerine kaydetme özelliği.

[ ] Emoji Reaksiyonları: Kullanıcıların haberlere ve hikayelere emoji ile tepki verebilmesi.

[ ] Yorum Sistemi: Her haberin altında kullanıcıların tartışabileceği bir yorum bölümü.

[ ] Canlıya Alma (Deployment): Projenin Vercel (frontend) ve Render (backend) gibi platformlar kullanılarak bir alan adına (domain) yayınlanması.
