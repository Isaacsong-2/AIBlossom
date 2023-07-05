let host = "http://localhost:8080"
$(document).ready(function () {
    const auth = getToken();
    if (auth !== undefined && auth !== '') {
        $.ajaxPrefilter(function (options, originalOptions, jqXHR) {
            jqXHR.setRequestHeader('Authorization', auth);
        });
        addProfile();
    } else {
        signupAndLogin();
    }
    loadFeeds();
    // addReference();
})

function getToken() {
    let auth = Cookies.get('Authorization');
    if (auth === undefined) {
        return '';
    }
    return auth;
}

function signupAndLogin() {
    var profile = document.getElementById('profile');
    var signupButton = document.createElement('button');
    var loginButton = document.createElement('button');
    signupButton.textContent = 'SignUp';
    loginButton.textContent = 'Login';

    signupButton.addEventListener('click', function () {
        window.location.href = "/blossom/user/signup"
    });

    loginButton.addEventListener('click', function () {
        window.location.href = '/blossom/user/login';
    });
    profile.appendChild(signupButton);
    profile.appendChild(loginButton);
}

function addProfile() {
    $.ajax({
        url: "http://localhost:8080/blossom/user-info",
        method: 'GET',
        success: function (response) {
            $('#profile').empty();
            $('#profile').append(`
                <div class="feed">
                    <div><span>프로필</span></div>
                    <div class="image">
                        <img src="${response.imageUrl}">
                    </div>
                     <div class="feedContent">
                        <h4>${response.username}</h4>
                        <div class="description">
                            <p>${response.introduction}</p>
                        </div>
                    </div>
                    <div>
                    <button onclick="createFeed()">피드 작성</button>
                    <button onclick="modifyProfile()">프로필 수정</button>
                    </div>
                </div>
            `)
        },
        error: function (error) {
            console.log('Error:', error);
        }
    })
}
function createFeed() {
    window.location.href = host + "/blossom/feed/manage"
}
function modifyProfile(){
    window.location.href = host + "/blossom/user/profile/manage"
}

function loadFeeds() {
    // 스크롤 이벤트 리스너 등록
    // 스크롤이 맨 아래로 내려갔을 때 새로운 피드 추가
    // 새로운 피드 가져오기
    $.ajax({
        url: 'http://localhost:8080/blossom/feed',
        method: 'GET',
        success: function (response) {
            console.log(response)
            $('.card').empty();
            // 피드 카드 생성 및 추가
            response.forEach(feed => {
                let addHtml = addHTML(feed);
                $('.card').append(addHtml)
            });
        },
        error: function (error) {
            console.log('Error:', error);
        }
    });

    function addHTML(feed) {
        /**
         * class="search-itemDto" 인 녀석에서
         * image, title, lprice, addProduct 활용하기
         * 참고) onclick='addProduct(${JSON.stringify(itemDto)})'
         */
        return `<div class="feed">
                    <a class="feedHref" href="http://localhost:8080/blossom/feed/${feed.id}">
                        <div class="image">
                            <img src="">
                        </div>
                    </a>
                    <div class="feedContent">
                        <a class="feedHref" href="http://localhost:8080/blossom/feed/${feed.id}">
                            <h4>${feed.title}</h4>
                            <div class="description">
                                <p>${feed.content}</p>
                            </div>
                            <div> <p> 좋아요 개수: ${feed.heartNum}</p></div>
                        </a>
                        <div class="subInfo">
                            <span> ${feed.modifiedAt} </span>
                            <span class="seperator"></span>
                        </div>
                    </div>
                    <div class="feedUser">
                        <a class="userInfo" href="">
                            <span>
                                "by "
                                <b>${feed.username}</b>
                            </span>
                        </a>
                    </div>
                </div>
                `
    }

}